package com.example.burningkey.multiplayer;

import com.example.burningkey.multiplayer.room.RoomSocketEventListener;
import com.example.burningkey.multiplayer.rooms.RoomsSocketEventListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new RoomsSocketEventListener(), "/multiplayer/rooms")
                .setAllowedOrigins("*");
        registry.addHandler(new RoomSocketEventListener(), "/multiplayer/rooms/room")
                .setAllowedOrigins("*");
    }
}