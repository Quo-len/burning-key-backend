package com.example.burningkey.auth.api.controller;

import com.example.burningkey.auth.service.AuthenticationService;
import com.example.burningkey.token.entity.Token;
import com.example.burningkey.token.repository.TokenRepository;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin // cross domain tomcat's port 8080 and react's 3000
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @PostMapping("/signin/{email}")
    public  ResponseEntity<String> signin(@PathVariable String email) {

        Optional<User> userOpt = userRepository.findByEmail(email);
        User user = userOpt.orElseGet(() -> authenticationService.register(email));
        var jwtToken = authenticationService.generateToken(user);
        Token t = new Token();
        t.setToken(jwtToken);
        tokenRepository.save(t);
        authenticationService.sendAuthenticationEmail(user.getEmail(), jwtToken);

        return new ResponseEntity<>("Token sent to email", HttpStatus.OK);
    }

   /* @GetMapping("/signin/{token}")
    public ResponseEntity<String> signin (@RequestParam("uid") String email, @PathVariable("token") String token) {

        try {
            authenticationService.authenticate(email, token);
            return new ResponseEntity<>("redirect", HttpStatus.OK);
        }
        catch (BadCredentialsException aBadCredentialsException) {
            return new ResponseEntity<>("no nononoon", HttpStatus.CONFLICT);
        }


    //    return ResponseEntity.ok(authenticationService.authenticate(email));
    }*/

    @GetMapping("/authenticate/{token}")
    public ResponseEntity<String> signin (@RequestParam("uid") String email, @PathVariable("token") String token) {
        try {
            authenticationService.authenticate(email, token);
            return new ResponseEntity<>("redirect", HttpStatus.OK);
        }
        catch (BadCredentialsException aBadCredentialsException) {
            return new ResponseEntity<>("no no no no", HttpStatus.I_AM_A_TEAPOT);
        }
    }

}
