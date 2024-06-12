package com.example.burningkey.user_lessons.api.controller;

import com.example.burningkey.user_lessons.api.dto.UserLessonDto;
import com.example.burningkey.user_lessons.entity.UserLesson;
import com.example.burningkey.user_lessons.service.UserLessonService;
import com.example.burningkey.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-lessons")
@CrossOrigin // cross domain tomcat's port 8080 and react's 3000
public class UserLessonController {

    @Autowired
    private UserLessonService userLessonService;

    @Autowired
    private UserService userService;

    @PostMapping("/populate")
    public ResponseEntity<Void> populateUsersWithLessons() {
        userLessonService.populateLessonsToUsers();
        return null;
    }

    @GetMapping
    public ResponseEntity<List<UserLessonDto>> getUserLessons(@RequestParam(required = false) Long userId, @RequestParam(required = false) LocalDate date) {
        List<UserLessonDto> userLessonDtos = userLessonService.getUserLessons(userId, date).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userLessonDtos);
    }

    @PostMapping("/add-lesson/{userId}")
    public ResponseEntity<String> smartAddUserLesson(@PathVariable Long userId, @RequestBody(required = true) UserLessonDto newUserLessonDto) {
        UserLesson newUserLesson = convertToEntity(newUserLessonDto);
        userLessonService.AddNewUserLesson(userId, newUserLesson);
        return ResponseEntity.ok("Success: added new lesson entry");
    }

    @PostMapping
    public ResponseEntity<UserLessonDto> createUserLesson(@RequestBody UserLessonDto userLessonDto) {
        UserLesson newUserLesson = convertToEntity(userLessonDto);
        newUserLesson = userLessonService.createUserLesson(newUserLesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(newUserLesson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserLessonDto> updateUserLesson(@PathVariable Long id, @RequestBody UserLessonDto userLessonDto) {
        UserLesson newUserLessonDto = convertToEntity(userLessonDto);
        Optional<UserLesson> updatedUserLessonDto = userLessonService.updateUserSession(id, newUserLessonDto);
        return updatedUserLessonDto.map(userLesson -> ResponseEntity.ok(convertToDto(userLesson)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserLesson(@PathVariable Long id) {
        boolean deleted = userLessonService.deleteUserLesson(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private UserLessonDto convertToDto(UserLesson userLesson) {
        UserLessonDto userLessonDto = new UserLessonDto();
        userLessonDto.setId(userLesson.getId());
        userLessonDto.setDate(userLesson.getDate());
        userLessonDto.setUserDto(userService.convertToDto(userLesson.getUser()));
        userLessonDto.setTimeSpent(userLesson.getTimeSpent());
        userLessonDto.setAverageSpeedWpm(userLesson.getAverageSpeedWpm());
        userLessonDto.setAverageAccuracy(userLesson.getAverageAccuracy());
        return userLessonDto;
    }

    private UserLesson convertToEntity(UserLessonDto userLessonDto) {
        UserLesson userLesson = new UserLesson();
        userLesson.setId(userLessonDto.getId());
        userLesson.setDate(userLessonDto.getDate());
        if(userLessonDto.getUserDto() != null)
            userLesson.setUser(userService.convertToEntity(userLessonDto.getUserDto()));
        else
            userLesson.setUser(null);
        userLesson.setTimeSpent(userLessonDto.getTimeSpent());
        userLesson.setAverageSpeedWpm(userLessonDto.getAverageSpeedWpm());
        userLesson.setAverageAccuracy(userLessonDto.getAverageAccuracy());
        return userLesson;
    }


}
