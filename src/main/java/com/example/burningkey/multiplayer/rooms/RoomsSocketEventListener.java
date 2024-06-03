package com.example.burningkey.multiplayer.rooms;

import com.example.burningkey.multiplayer.service.RoomDTO;
import com.example.burningkey.multiplayer.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomsSocketEventListener extends TextWebSocketHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private List<WebSocketSession> openSessions = Collections.synchronizedList(new ArrayList<>());
    private RoomService roomService = RoomService.roomService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Open session");
        openSessions.add(session);

        List<RoomDTO> activeRooms = roomService.getRooms().stream()
                .map(room -> RoomDTO.builder()
                        .uid(room.getUid())
                        .title(room.getTitle())
                        .build())
                .toList();

        if (activeRooms.size() > 0) {
            sendBroadcastMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                    "type", "CONNECT",
                    "data", activeRooms
            ))));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> clientMessage = objectMapper.readValue(message.getPayload(), Map.class);

        String type = (String) clientMessage.get("type");
        String username = (String) clientMessage.get("username");

        session.getAttributes().put("username", username);
        Random random = new Random();
        int randomNumber = random.nextInt(0, 1000);
        AtomicInteger startTimer = new AtomicInteger(5);
        switch (type) {
            case "CREATE":
                RoomDTO room = roomService.createRoom(username + " " + randomNumber, startTimer);
                RoomDTO newRoom = RoomDTO.builder()
                        .uid(room.getUid())
                        .title(username + " " + randomNumber)
                        .start(startTimer)
                        .build();
                sendBroadcastMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                        "type", "DATA",
                        "data", newRoom
                ))));
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Close session");
        openSessions.remove(session);
    }


    private void sendBroadcastMessage(TextMessage textMessage) throws IOException {
        for (WebSocketSession session : openSessions) {
            session.sendMessage(textMessage);
        }
    }

}
