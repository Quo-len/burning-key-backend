package com.example.burningkey.multiplayer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RoomService {

    public static RoomService roomService;

    private List<RoomDTO> rooms = Collections.synchronizedList(new ArrayList<>());

    public RoomService() {
        roomService = this;
    }

    public RoomDTO createRoom(String title, AtomicInteger timer) {
        RoomDTO newRoom = RoomDTO.builder()
                .uid(getUUID())
                .activeUsers(Collections.synchronizedList(new ArrayList<>()))
                .start(timer)
                .title(title)
                .build();

        rooms.add(newRoom);

        return newRoom;
    }

    public void removeRoom(String uid) {
        if (rooms.size() > 0) {
            rooms.removeIf(v -> v.getUid().equals(uid));
        }
    }

    public RoomDTO addMember(String username, String uid, WebSocketSession session) throws Exception {
        UserDTO newUser = UserDTO.builder()
                .username(username)
                .session(session)
                .sessionId(session.getId())
                .build();

        RoomDTO room = rooms.stream()
                .filter(v -> v.getUid().equals(uid))
                .findFirst()
                .orElseThrow(() -> new Exception(String.format("Room with id %s not found", uid)));
        room.getActiveUsers().add(newUser);

        return room;
    }

    public UserDTO removeMember(String uid, WebSocketSession session) throws Exception {
        RoomDTO currentRoom = rooms.stream()
                .filter(v -> v.getUid().equals(uid))
                .findFirst().orElseThrow(() -> new Exception(String.format("Cannot find room with %s id", uid)));

        UserDTO userDTO = currentRoom.getActiveUsers().stream()
                .filter(v -> v.getSession().equals(session))
                .findFirst()
                .orElseThrow();
        currentRoom.getActiveUsers().remove(userDTO);

        return userDTO;
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }

}
