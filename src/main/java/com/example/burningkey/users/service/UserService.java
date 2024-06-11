package com.example.burningkey.users.service;

import com.example.burningkey.users.api.dto.UserDto;
import com.example.burningkey.users.entity.Role;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void addUsers() {

        userRepository.save(User.builder()
                .email("admin@example.com")
                .nickname("admin")
                .role(Role.ADMIN)
                .build());

        userRepository.save(User.builder()
                .email("user@example.com")
                .nickname("User1")
                .role(Role.USER)
                .build());

        userRepository.save(User.builder()
                .email("user2@example.com")
                .nickname("User2")
                .role(Role.USER)
                .build());
    }

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

    public void saveImage(User user , MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            int dotIndex = fileName.lastIndexOf('.');
            String extension = (dotIndex > 0) ? fileName.substring(dotIndex + 1) : "";

            fileName = user.getEmail() + "." + extension;

            Path resourceDirectory = Paths.get("src","main", "resources", "images");
            String absolutePath = resourceDirectory.toFile().getAbsolutePath();

            if (!Files.exists(Path.of(absolutePath))) {
                Files.createDirectories(Path.of(absolutePath));
            }

            Path filePath = Paths.get(absolutePath, fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            Files.copy(file.getInputStream(), filePath);

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
