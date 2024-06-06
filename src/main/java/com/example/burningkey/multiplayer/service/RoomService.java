package com.example.burningkey.multiplayer.service;

import com.example.burningkey.multiplayer.rooms.RoomsSocketEventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
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
                .playersPosition(new HashMap<>())
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

    public boolean isRoomExist(String uid) {
        return rooms.stream()
                .anyMatch(v -> v.getUid().equals(uid));
    }
    public RoomDTO getRoomById(String uid) {
        return rooms.stream()
                .filter(v -> v.getUid().equals(uid))
                .findFirst()
                .orElseThrow();
    }
    public RoomDTO addMember(String username, String uid, WebSocketSession session) {
        UserDTO newUser = UserDTO.builder()
                .username(username)
                .session(session)
                .sessionId(session.getId())
                .completeText(0.0)
                .currentWordPosition(0)
                .build();

        RoomDTO room = null;
        try {
            room = rooms.stream()
                    .filter(v -> v.getUid().equals(uid))
                    .findFirst()
                    .orElseThrow(() -> new Exception(String.format("Room with id %s not found", uid)));
            room.getActiveUsers().add(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        if (currentRoom.getActiveUsers().size() <= 0) {
            rooms.remove(currentRoom);
            RoomsSocketEventListener.roomsSocketEventListener.broadCastRemoveRoom(currentRoom);
        }

        return userDTO;
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

}
