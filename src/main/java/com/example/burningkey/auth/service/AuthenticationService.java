package com.example.burningkey.auth.service;

import com.example.burningkey.auth.entity.AuthenticationResponse;
import com.example.burningkey.auth.entity.RegisterRequest;
import com.example.burningkey.securingweb.JwtService;
import com.example.burningkey.token.entity.Token;
import com.example.burningkey.token.entity.TokenType;
import com.example.burningkey.token.repository.TokenRepository;
import com.example.burningkey.users.entity.Role;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;

    public void sendAuthenticationEmail(String email, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your signin link");
        mailMessage.setText(String.format("Hello!\nAccess your account here: http://localhost:8080/api/v1/auth/authenticate/%s\nExpires in 10 minutes", token));
        mailSender.send(mailMessage);
    }

    public User register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    public AuthenticationResponse authenticateWithToken(String token) {
        String email = jwtService.extractUsername(token);
        var user = userRepository.findByEmail(email).orElseThrow();
        if (validateWelcomeToken(token)) {
            var jwtToken = jwtService.generateToken(user, 720,0,0); // 30 days
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken, TokenType.BEARER);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
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
