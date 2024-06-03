package com.example.burningkey.token.api.dto;

import com.example.burningkey.token.entity.TokenType;
import com.example.burningkey.users.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDto {
    public Integer id;
    public String token;
    public String email;
    public TokenType tokenType = TokenType.BEARER;
    public boolean revoked;
    public boolean expired;
    public User user;
}
