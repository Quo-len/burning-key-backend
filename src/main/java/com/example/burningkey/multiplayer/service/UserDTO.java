package com.example.burningkey.multiplayer.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    private String username;
    private Integer currentWordPosition;
    private String currentWord;
    private Double completeText;
    private String sessionId;
    @JsonIgnore
    private WebSocketSession session;
}
