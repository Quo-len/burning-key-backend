package com.example.burningkey.users.api.controller;

import com.example.burningkey.users.api.dto.UserDto;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin // cross domain tomcat's port 8080 and react's 3000
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = userService.getAllTexts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getTextById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUserById(id);
        return optionalUser.map(user -> ResponseEntity.ok(convertToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new text
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User newUser = convertToEntity(userDto);
        newUser = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(newUser));
    }

    // Update an existing text
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto newUserDto) {
        User newUser = convertToEntity(newUserDto);
        Optional<User> updatedUser = userService.updateUser(id, newUser);
        return updatedUser.map(text -> ResponseEntity.ok(convertToDto(text)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a text
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Convert Entity to DTO
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    // Convert DTO to Entity
    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUsername(userDto.getEmail());
        user.setEmail(userDto.getEmail());
        return user;
    }

}
