package com.example.burningkey.users.service;

import com.example.burningkey.auth.entity.AuthenticationResponse;
import com.example.burningkey.auth.entity.RegisterRequest;
import com.example.burningkey.auth.service.AuthenticationService;
import com.example.burningkey.token.entity.TokenType;
import com.example.burningkey.user_lessons.entity.UserLesson;
import com.example.burningkey.user_lessons.repository.UserLessonRepository;
import com.example.burningkey.user_lessons.service.UserLessonService;
import com.example.burningkey.users.api.dto.UserDto;
import com.example.burningkey.users.entity.Role;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User createUser(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .role(Role.USER)
                .nickname(generateNickname())
                .imageUrl("default.png")
                .build();
        return createUser(user);
    }

    public Optional<User> updateUser(Long id, User newUser) {
        return userRepository.findById(id).map(existingUser -> {
            if (newUser.getNickname() != null) existingUser.setNickname(newUser.getNickname());
            if (newUser.getEmail() != null) existingUser.setEmail(newUser.getEmail());
            if (newUser.getRole() != null) existingUser.setRole(newUser.getRole());
            if (newUser.getImageUrl() != null) existingUser.setImageUrl(newUser.getImageUrl());
            return userRepository.save(existingUser);
        });
    }

    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    public void saveImage(User user, MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            int dotIndex = fileName.lastIndexOf('.');
            String extension = (dotIndex > 0) ? fileName.substring(dotIndex + 1) : "";

            if (extension.equals("jpg"))
                extension = "jpeg";

            fileName = user.getEmail() + "." + extension;

            Path resourceDirectory = Paths.get("src", "main", "java", "com", "example", "burningkey", "users", "images");
            String absolutePath = resourceDirectory.toFile().getAbsolutePath();
            Path filePath = Paths.get(absolutePath, fileName);

            Path filePathPng = Paths.get(absolutePath, user.getEmail() + ".png");
            Path filePathGif = Paths.get(absolutePath, user.getEmail() + ".gif");
            Path filePathJpeg = Paths.get(absolutePath, user.getEmail() + ".jpeg");

            if (Files.exists(filePathPng)) {
                Files.delete(filePathPng);
            }
            if (Files.exists(filePathGif)) {
                Files.delete(filePathGif);
            }
            if (Files.exists(filePathJpeg)) {
                Files.delete(filePathJpeg);
            }

            try (InputStream inputStream = file.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(filePath.toFile(), false)) { // Set to write mode
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }

            user.setImageUrl(fileName);
            updateUser(user.getId(), user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    public String generateNickname() {
        Resource adjectivesResource = new ClassPathResource("nicknames/adjectives.txt"); // 1143
        Resource animalsResource = new ClassPathResource("nicknames/animals.txt"); // 354

        final int adjectivesCount = 1143;
        final int animalsCount = 354;

        File adjectivesFile;
        File animalsFile;
        try {
            adjectivesFile = adjectivesResource.getFile();
            animalsFile = animalsResource.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String adjectivesPath = adjectivesFile.getAbsolutePath();
        String animalsPath = animalsFile.getAbsolutePath();

        Random rand = new Random();
        int randIdx = rand.nextInt(0, adjectivesCount);
        String adjective = "";
        try (BufferedReader br = new BufferedReader(new FileReader(adjectivesPath))) {
            for (int i = 0; i <= randIdx; i++) {
                adjective = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        randIdx = rand.nextInt(0, animalsCount);
        String animal = "";
        try (BufferedReader br = new BufferedReader(new FileReader(animalsPath))) {
            for (int i = 0; i <= randIdx; i++) {
                animal = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String nickname = adjective.substring(0, 1).toUpperCase() + adjective.substring(1) + " " + animal.substring(0, 1).toUpperCase() + animal.substring(1);

        return nickname.trim();
    }

    // Convert Entity to DTO
    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setNickname(user.getNickname());
        userDto.setEmail(user.getEmail());
        userDto.setImageUrl(user.getImageUrl());
        return userDto;
    }

    // Convert DTO to Entity
    public User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getUserId());
        user.setNickname(userDto.getNickname());
        user.setEmail(userDto.getEmail());
        user.setImageUrl(userDto.getImageUrl());
        return user;
    }

}