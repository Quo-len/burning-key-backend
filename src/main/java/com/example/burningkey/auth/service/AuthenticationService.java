package com.example.burningkey.auth.service;

import com.example.burningkey.auth.entity.AuthenticationResponse;
import com.example.burningkey.auth.entity.RegisterRequest;
import com.example.burningkey.securingweb.JwtService;
import com.example.burningkey.token.entity.Token;
import com.example.burningkey.token.entity.TokenType;
import com.example.burningkey.token.repository.TokenRepository;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JavaMailSender mailSender;

    @PostConstruct
    public void addUsers() {
        User user1 = register(new RegisterRequest("parfesa.oleksandr1122@vu.cdu.edu.ua"));
        generateToken(user1, TokenType.WELCOME, 0, 10, 0);

        User user2 = register(new RegisterRequest("no email"));
        generateToken(user2, TokenType.WELCOME, 0, 10, 0);

        User user3 = register(new RegisterRequest("kotenko.vadym1121@vu.cdu.edu.ua"));
        generateToken(user3, TokenType.WELCOME, 0, 10, 0);

        User user4 = register(new RegisterRequest("bondar.vladislav1121@vu.cdu.edu.ua"));
        generateToken(user4, TokenType.WELCOME, 0, 10, 0);

        User user5 = register(new RegisterRequest("povzun.andrii1121@vu.cdu.edu.ua"));
        generateToken(user5, TokenType.WELCOME, 0, 10, 0);

        User user6 = register(new RegisterRequest("insert email here"));
        generateToken(user6, TokenType.WELCOME, 0, 10, 0);

        for (int i = 0; i < 12; i++) {
            User newUser = register(new RegisterRequest("user" + i));
            generateToken(newUser, TokenType.WELCOME, 0, 10, 0);
        }
    }

    public void sendAuthenticationEmail(String email, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your signin link");
        mailMessage.setText(String.format("Hello!\nAccess your account here: http://25.36.165.69:8080/api/v1/auth/authenticate/%s\nExpires in 10 minutes", token));
        mailSender.send(mailMessage);
    }

    public User register(RegisterRequest request) {
        return userService.createUser(request);
    }

    public AuthenticationResponse authenticateWithToken(String token) {
        String email = jwtService.extractUsername(token);
        var user = userService.getUserByEmail(email).orElseThrow();
        if (validateWelcomeToken(token)) {
            var jwtToken = jwtService.generateToken(user, 720,0,0); // 30 days
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken, TokenType.BEARER);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .user(userService.convertToDto(user))
                    .build();
        } else {
            throw new RuntimeException("Invalid token");
        }
    }

    public String generateToken(User user, TokenType tokenType, int hours, int minutes, int seconds) {
        var jwtToken = jwtService.generateToken(user, hours, minutes, seconds);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken, tokenType);
        return jwtToken;
    }

    public boolean validateWelcomeToken(String token) {
        Optional<Token> tokenOpt = tokenRepository.findByToken(token);
        String email = jwtService.extractUsername(token);
        if (tokenOpt.isPresent()) {
            Token foundToken = tokenOpt.get();
            boolean isValid = foundToken.getTokenType() == TokenType.WELCOME && !foundToken.isExpired() && !foundToken.isRevoked() && foundToken.getUser().getEmail().equals(email);
            foundToken.setExpired(true);
            foundToken.setExpired(true);
            return isValid;
        }
        return false;
    }

    private void saveUserToken(User user, String jwtToken, TokenType tokenType) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }
}
