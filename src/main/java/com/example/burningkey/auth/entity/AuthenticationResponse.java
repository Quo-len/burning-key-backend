package com.example.burningkey.auth.entity;

import com.example.burningkey.users.api.dto.UserDto;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {

    private String token;
    private UserDto user;
}
