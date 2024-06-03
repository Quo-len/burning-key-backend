package com.example.burningkey.auth.entity;

import lombok.*;

/*@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter*/
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
}
