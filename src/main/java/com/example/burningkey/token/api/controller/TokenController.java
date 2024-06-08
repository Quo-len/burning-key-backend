package com.example.burningkey.token.api.controller;

import com.example.burningkey.token.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tokens")
@CrossOrigin // cross domain tomcat's port 8080 and react's 3000
public class TokenController {

    @Autowired
    private TokenRepository tokenRepository;

}
