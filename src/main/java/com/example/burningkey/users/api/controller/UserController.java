package com.example.burningkey.users.api.controller;

import com.example.burningkey.securingweb.JwtService;
import com.example.burningkey.users.api.dto.UserDto;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin // cross domain tomcat's port 8080 and react's 3000
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = userService.getAllUsers().stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

   /* @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUserById(id);
        return optionalUser.map(user -> ResponseEntity.ok(convertToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }    */

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<User> optionalUser = userService.getUserByEmail(email);
        return optionalUser.map(user -> ResponseEntity.ok(userService.convertToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-token/{token}")
    public ResponseEntity<UserDto> getUserByToken(@PathVariable String token) {
        String userEmail = jwtService.extractUsername(token);
        Optional<User> optionalUser = userService.getUserByEmail(userEmail);
        return optionalUser.map(user -> ResponseEntity.ok(userService.convertToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new text
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User newUser = userService.convertToEntity(userDto);
        newUser = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToDto(newUser));
    }

    // Update an existing user
    @GetMapping("/nickname")
    public ResponseEntity<String> getNewNickname() {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.generateNickname());
    }

    // Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto newUserDto) {
        User newUser = userService.convertToEntity(newUserDto);
        Optional<User> updatedUser = userService.updateUser(id, newUser);
        return updatedUser.map(user -> ResponseEntity.ok(userService.convertToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();

        userService.saveImage(user, file);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/image/{userId}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long userId) throws IOException {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty() || userOptional.get().getImageUrl() == null) {
            return ResponseEntity.notFound().build(); // User not found or no image URL
        }
        User user = userOptional.get();

        String imageUrl = userOptional.get().getImageUrl();
        int dotIndex = imageUrl.lastIndexOf('.');
        String extension = (dotIndex > 0) ? imageUrl.substring(dotIndex + 1) : "";

        MediaType mediaType = MediaType.IMAGE_PNG;
        mediaType = switch (extension) {
            case ".gif" -> MediaType.IMAGE_GIF;
            case "jpeg" -> MediaType.IMAGE_JPEG;
            default -> mediaType;
        };

        Path resourceDirectory = Paths.get("src","main", "java", "com", "example", "burningkey", "users", "images");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        String filePath = absolutePath + "/" + user.getImageUrl();
        System.out.println("file path " + filePath);
        try {
            byte[] imageData = Files.readAllBytes(Path.of(filePath));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            return ResponseEntity.ok().headers(headers).contentType(mediaType).body(imageData);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
