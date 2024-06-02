package com.example.burningkey.multiplayer.service;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class RoomDTO {

    private List<UserDTO> activeUsers;
    private String uid;
    private String title;

}
