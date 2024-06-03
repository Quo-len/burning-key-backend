package com.example.burningkey.auth.entity;

import lombok.*;

/*@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
