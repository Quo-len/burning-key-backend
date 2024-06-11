package com.example.burningkey.users.api.controller;

import com.example.burningkey.securingweb.JwtService;
import com.example.burningkey.texts.api.dto.TextDto;
import com.example.burningkey.texts.entity.Text;
import com.example.burningkey.users.api.dto.UserDto;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.nio.file.Files;
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

    @PostMapping("/{id}/image")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();

        userService.saveImage(user, file);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long userId) throws IOException {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty() || userOptional.get().getImageUrl() == null) {
            return ResponseEntity.notFound().build(); // User not found or no image URL
        }

        String imageUrl = userOptional.get().getImageUrl();

        Resource resource = new ClassPathResource("images/" + imageUrl);
        System.out.println("root + url " + resource.getFile().getAbsoluteFile());
        try {
            byte[] imageData = Files.readAllBytes(resource.getFile().toPath());

            return ResponseEntity.ok().body(imageData);
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
