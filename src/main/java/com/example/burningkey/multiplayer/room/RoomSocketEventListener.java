package com.example.burningkey.multiplayer.room;

import com.example.burningkey.multiplayer.service.RoomDTO;
import com.example.burningkey.multiplayer.service.RoomService;
import com.example.burningkey.multiplayer.service.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

public class RoomSocketEventListener extends TextWebSocketHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private RoomService roomService = RoomService.roomService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Open session");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> clientMessage = objectMapper.readValue(message.getPayload(), Map.class);

        String type = (String) clientMessage.get("type");
        String uid = (String) clientMessage.get("uid");


        switch (type) {
            case "CONNECT":
                Integer durationToStart = 5;

                String username = (String) clientMessage.get("username");

                session.getAttributes().put("uid", uid);
                RoomDTO room = roomService.addMember(username, uid, session);

                UserDTO user = UserDTO.builder()
                        .sessionId(session.getId())
                        .username(username)
                        .build();
                if (room.getActiveUsers().size() >= 2) {
                    sendBroadcastMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                            "type", "TIMER",
                            "duration", durationToStart
                    ))), uid);
                }
                sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                        "type", "CONNECT_USER",
                        "data", room.getActiveUsers(),
                        "sessionId", session.getId()
                ))), session, uid);

                sendBroadcastMessageExcept(new TextMessage(objectMapper.writeValueAsString(Map.of(
                        "type", "CONNECT",
                        "data", user
                ))), session, uid);
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
