package com.example.burningkey.user_statistics.api.controller;

import com.example.burningkey.user_statistics.api.dto.UserStatisticDto;
import com.example.burningkey.user_statistics.entity.UserStatistic;
import com.example.burningkey.user_statistics.service.UserStatisticService;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-statistics")
@CrossOrigin // cross domain tomcat's port 8080 and react's 3000
public class UserStatisticController {

    @Autowired
    private UserStatisticService userStatisticService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserStatisticDto>> getAllUserStatistics() {
        List<UserStatisticDto> userStatisticDtos = userStatisticService.getAllUserStatistics().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userStatisticDtos);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserStatisticDto>> getTop1000ByBestWpmAndAccuracy() {
        List<UserStatisticDto> leaderboard = userStatisticService.getTop1000ByBestWpmAndAccuracy().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserStatisticDto> getUserStatisticsByUserId(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        User foundUser = optionalUser.get();
        Optional<UserStatistic> optionalUserStatistic = userStatisticService.getUserStatisticByUserId(foundUser);
        return optionalUserStatistic.map(userStatistic -> ResponseEntity.ok(convertToDto(userStatistic)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Convert Entity to DTO
    public UserStatisticDto convertToDto(UserStatistic userStatistic) {
        UserStatisticDto userStatisticDto = new UserStatisticDto();
        userStatisticDto.setId(userStatistic.getId());
        userStatisticDto.setTotalSessions(userStatistic.getTotalSessions());
        userStatisticDto.setTotalLessons(userStatistic.getTotalLessons());
        userStatisticDto.setTotalTimeSpent(userStatistic.getTotalTimeSpent());
        userStatisticDto.setUserDto(userService.convertToDto(userStatistic.getUser()));
        userStatisticDto.setAverageSpeedWpm(userStatistic.getAverageSpeedWpm());
        userStatisticDto.setAverageAccuracy(userStatistic.getAverageAccuracy());
        userStatisticDto.setBestSpeedWpm(userStatistic.getBestSpeedWpm());
        userStatisticDto.setBestAccuracy(userStatistic.getBestAccuracy());
        return userStatisticDto;
    }

    // Convert DTO to Entity
    public UserStatistic convertToEntity(UserStatisticDto userStatisticDto) {
        UserStatistic userStatistic = new UserStatistic();
        userStatistic.setId(userStatisticDto.getId());
        userStatistic.setTotalSessions(userStatisticDto.getTotalSessions());
        userStatistic.setTotalLessons(userStatisticDto.getTotalLessons());
        userStatistic.setTotalTimeSpent(userStatisticDto.getTotalTimeSpent());
        userStatistic.setUser(userService.convertToEntity(userStatisticDto.getUserDto()));
        userStatistic.setAverageSpeedWpm(userStatisticDto.getAverageSpeedWpm());
        userStatistic.setAverageAccuracy(userStatisticDto.getAverageAccuracy());
        userStatistic.setBestSpeedWpm(userStatisticDto.getBestSpeedWpm());
        userStatistic.setBestAccuracy(userStatisticDto.getBestAccuracy());
        return userStatistic;
    }

}
