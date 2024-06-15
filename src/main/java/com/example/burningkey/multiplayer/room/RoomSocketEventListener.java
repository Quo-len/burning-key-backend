package com.example.burningkey.multiplayer.room;

import com.example.burningkey.multiplayer.rooms.RoomsSocketEventListener;
import com.example.burningkey.multiplayer.service.RoomDTO;
import com.example.burningkey.multiplayer.service.RoomService;
import com.example.burningkey.multiplayer.service.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RoomSocketEventListener extends TextWebSocketHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private RoomService roomService = RoomService.roomService;
    private HashMap<String, ExecutorService> timer = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Open session");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> clientMessage = objectMapper.readValue(message.getPayload(), Map.class);

        String uid = (String) clientMessage.get("uid");

//        ExecutorService executor = roomThread.computeIfAbsent(uid, k -> Executors.newSingleThreadExecutor());
        try {
            ExecutorService executor = roomService.getRoomById(uid).getExecutorService();
            executor.submit(() -> {
                try {
                    handleRoomAction(session, clientMessage, uid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (NoSuchElementException e) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                    "type", "EXPIRED_ROOM"
            ))));
            e.printStackTrace();
        }


    }

    private void handleRoomAction(WebSocketSession session, Map<String, Object> clientMessage, String uid) throws IOException, InterruptedException {

        String type = (String) clientMessage.get("type");


        switch (type) {
            case "CONNECT":
                RoomDTO roomCheck = roomService.getRoomById(uid);
                if (roomCheck.getActiveUsers().size() >= roomCheck.getMaxAmountOfPlayers()) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                            "type", "ROOM_FILLED"
                    ))));
                    return;
                }
                String username = (String) clientMessage.get("username");
                session.getAttributes().put("uid", uid);
                String userImage = (String) clientMessage.get("userImage");
                RoomDTO room = roomService.addMember(username, uid, session, userImage);
                UserDTO user = UserDTO.builder()
                        .sessionId(session.getId())
                        .username(username)
                        .userImage(userImage)
                        .build();

                if (room.getStart().get() <= 0) {
                    sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                            "type", "STARTED",
                            "startedAt", room.getStartedAt()
                    ))), session, uid);
                    return;
                }
                sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                        "type", "CONNECT_USER",
                        "data", room.getActiveUsers(),
                        "sessionId", session.getId(),
                        "text", room.getText()
                ))), session, uid);
                sendBroadcastMessageExcept(new TextMessage(objectMapper.writeValueAsString(Map.of(
                        "type", "CONNECT",
                        "data", user
                ))), session, uid);
                if (room.getActiveUsers().size() >= 2) {
                    room.setTimerCountDown(true);
                    RoomsSocketEventListener.roomsSocketEventListener.broadCastUserConnect(uid, user.getUsername(), room.isTimerCountDown());
                } else {
                    RoomsSocketEventListener.roomsSocketEventListener.broadCastUserConnect(uid, user.getUsername(), room.isTimerCountDown());
                }

                ExecutorService executorService = timer.computeIfAbsent(uid, k -> Executors.newSingleThreadExecutor());
                timer.put(uid, executorService);
                if (room.getActiveUsers().size() >= 2) {
                    executorService.submit(() -> {
                        while (room.getStart().get() >= 0) {
                            try {
                                sendBroadcastMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                                        "type", "TIMER",
                                        "duration", room.getStart().getAndDecrement()
                                ))), uid);
                                TimeUnit.SECONDS.sleep(1);
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        room.setStartedAt(System.currentTimeMillis());
                    });
                }
                timer.remove(uid);
                break;
            case "DATA":
                String currentWord = (String) clientMessage.get("currentWord");
                Integer currentWordPosition = (Integer) clientMessage.get("wordCount");
                String newSpeed = String.format("%.2f", Double.parseDouble((String) clientMessage.get("newSpeed")));
                sendBroadcastMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                        "type", "DATA",
                        "currentWord", currentWord,
                        "currentWordPosition", currentWordPosition,
                        "sessionId", session.getId(),
                        "newSpeed", newSpeed
                ))), uid);

                break;
            case "END_RACE":
                sendBroadcastMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                        "type", "END_RACE",
                        "userId", session.getId()
                ))), uid);
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Close session");

        String uid = (String) session.getAttributes().get("uid");
        UserDTO userDTO = roomService.removeMember(uid, session);
        sendBroadcastMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                "type", "DISCONNECT",
                "data", userDTO
        ))), uid);
    }


    private void sendBroadcastMessageExcept(TextMessage textMessage, WebSocketSession session, String uid) throws IOException {

        try {
            RoomDTO room = roomService.getRooms().stream()
                    .filter(v -> v.getUid().equals(uid))
                    .findFirst()
                    .orElseThrow();
            room.getActiveUsers().stream()
                    .filter(v -> !v.getSession().equals(session))
                    .map(UserDTO::getSession)
                    .forEach(v -> {
                        try {
                            v.sendMessage(textMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

    }

    private void sendBroadcastMessage(TextMessage textMessage, String uid) throws IOException {
        RoomDTO room = roomService.getRooms().stream()
                .filter(v -> v.getUid().equals(uid))
                .findFirst()
                .orElseThrow();

        room.getActiveUsers().stream()
                .map(UserDTO::getSession)
                .forEach(v -> {
                    try {
                        v.sendMessage(textMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void sendMessage(TextMessage textMessage, WebSocketSession session, String uid) throws IOException {
        RoomDTO rooms = roomService.getRooms().stream()
                .filter(v -> v.getUid().equals(uid))
                .findFirst()
                .orElseThrow();

        rooms.getActiveUsers().stream()
                .filter(v -> v.getSession().equals(session))
                .findFirst()
                .map(UserDTO::getSession)
                .orElseThrow()
                .sendMessage(textMessage);
    }
}
