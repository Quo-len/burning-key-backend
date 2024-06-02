package com.example.burningkey.auth.service;

import com.example.burningkey.auth.entity.AuthenticationResponse;
import com.example.burningkey.securingweb.JwtService;
import com.example.burningkey.token.repository.TokenRepository;
import com.example.burningkey.users.entity.Role;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final JavaMailSender mailSender;
    private final TokenRepository tokenRepository;

    public void sendAuthenticationEmail(String email, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your signin link");
        mailMessage.setText(String.format("%s\nHello!\nAccess your account here: http://localhost:8080/api/v1/auth/authenticate/%s?uid=%s",token,token,email));

        mailSender.send(mailMessage);
        System.out.println("HI");
    }

    public User register(String email) {
        var user = User.builder()
                .username(email)
                .email(email)
                .role(Role.USER)
                .build();
        repository.save(user);
        return user;
    }

    public String generateToken(User user) {
        return jwtService.generateToken(user);
    }

    public AuthenticationResponse authenticate(String email, String token) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        token
                )
        );
        var user = repository.findByEmail(email)
                .orElseThrow();
     //   var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}