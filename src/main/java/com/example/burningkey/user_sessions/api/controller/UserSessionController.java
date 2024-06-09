package com.example.burningkey.user_sessions.api.controller;

import com.example.burningkey.user_sessions.api.dto.UserSessionDto;
import com.example.burningkey.user_sessions.entity.UserSession;
import com.example.burningkey.user_sessions.service.UserSessionService;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-sessions")
@CrossOrigin // cross domain tomcat's port 8080 and react's 3000
public class UserSessionController {

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserSessionDto>> getAllUserSessions(Long userId, LocalDate date) {
        List<UserSessionDto> userStatisticDtos = userSessionService.getUserSessions(userId, date).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userStatisticDtos);
    }

    @GetMapping("/session")
    public ResponseEntity<UserSessionDto> getUserStatisticsByUserIdAndDate(@RequestParam Long userId, @RequestParam LocalDate date) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        User foundUser = optionalUser.get();
        Optional<UserSession> optionalUserSession = userSessionService.getUserSessionByUserAndDate(foundUser, date);
        return optionalUserSession.map(user -> ResponseEntity.ok(convertToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Convert Entity to DTO
    public UserSessionDto convertToDto(UserSession userSession) {
        UserSessionDto userSessionDto = new UserSessionDto();
        userSessionDto.setId(userSession.getId());
        userSessionDto.setDate(userSession.getDate());
        userSessionDto.setNumLessons(userSession.getNumLessons());
        userSessionDto.setTimeSpent(userSession.getTimeSpent());
        userSessionDto.setUserDto(userService.convertToDto(userSession.getUser()));
        userSessionDto.setAverageSpeedWpm(userSession.getAverageSpeedWpm());
        userSessionDto.setAverageAccuracy(userSession.getAverageAccuracy());
        userSessionDto.setBestSpeedWpm(userSession.getBestSpeedWpm());
        userSessionDto.setBestAccuracy(userSession.getBestAccuracy());
        return userSessionDto;
    }

    // Convert DTO to Entity
    public UserSession convertToEntity(UserSessionDto userSessionDto) {
        UserSession userSession = new UserSession();
        userSession.setId(userSessionDto.getId());
        userSession.setDate(userSessionDto.getDate());
        userSession.setNumLessons(userSessionDto.getNumLessons());
        userSession.setTimeSpent(userSessionDto.getTimeSpent());
        userSession.setUser(userService.convertToEntity(userSessionDto.getUserDto()));
        userSession.setAverageSpeedWpm(userSessionDto.getAverageSpeedWpm());
        userSession.setAverageAccuracy(userSessionDto.getAverageAccuracy());
        userSession.setBestSpeedWpm(userSessionDto.getBestSpeedWpm());
        userSession.setBestAccuracy(userSessionDto.getBestAccuracy());
        return userSession;
    }
}
