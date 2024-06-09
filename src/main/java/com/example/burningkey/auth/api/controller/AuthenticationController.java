package com.example.burningkey.auth.api.controller;

import com.example.burningkey.auth.entity.AuthenticationResponse;
import com.example.burningkey.auth.entity.RegisterRequest;
import com.example.burningkey.auth.service.AuthenticationService;
import com.example.burningkey.token.entity.TokenType;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/gimme-token/{email}")
    public ResponseEntity<AuthenticationResponse> getWorkingToken(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user = userOpt.orElseGet(() -> authenticationService.register(new RegisterRequest(email)));
        var jwtToken = authenticationService.generateToken(user, TokenType.WELCOME, 0, 10, 0);
       // authenticationService.sendAuthenticationEmail(user.getEmail(), jwtToken);
        AuthenticationResponse authResponse = authenticationService.authenticateWithToken(jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @PostMapping("/signin/{email}")
    public ResponseEntity<AuthenticationResponse> getLoginLink(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user = userOpt.orElseGet(() -> authenticationService.register(new RegisterRequest(email)));
        var jwtToken = authenticationService.generateToken(user, TokenType.WELCOME, 0, 10, 0);
        authenticationService.sendAuthenticationEmail(user.getEmail(), jwtToken);
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @PostMapping("/authenticate/{token}")
    public ResponseEntity<AuthenticationResponse> authorize(@PathVariable("token") String token) throws URISyntaxException {
        AuthenticationResponse authResponse = authenticationService.authenticateWithToken(token);
      /*  URI uri = new URI("http://localhost:5173/");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(httpHeaders).body(authResponse);*/
        AuthenticationResponse authResponse1 = new AuthenticationResponse();
        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(authResponse);
    }
}
