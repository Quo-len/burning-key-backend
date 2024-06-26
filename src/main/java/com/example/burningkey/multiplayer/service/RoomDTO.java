package com.example.burningkey.multiplayer.service;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class RoomDTO {

    private List<UserDTO> activeUsers;
    private String uid;
    private String title;
    private AtomicInteger start;
    private Map<String, Long> playersPosition;
    private long startedAt;
    private boolean isTimerCountDown;
    private Integer maxAmountOfPlayers;
    private String text;
    @JsonIgnore
    private ExecutorService executorService;

}
