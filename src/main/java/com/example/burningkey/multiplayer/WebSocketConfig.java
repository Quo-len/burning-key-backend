package com.example.burningkey.multiplayer;

import com.example.burningkey.multiplayer.room.RoomSocketEventListener;
import com.example.burningkey.multiplayer.rooms.RoomsSocketEventListener;
import jakarta.websocket.server.ServerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

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

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(2000000);
        container.setMaxBinaryMessageBufferSize(2000000);
        return container;
    }


}