package com.example.burningkey.users.service;

import com.example.burningkey.users.api.dto.UserDto;
import com.example.burningkey.users.entity.Role;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
            if (newUser.getNickname() != null) existingUser.setNickname(newUser.getUsername());
            if (newUser.getEmail() != null) existingUser.setEmail(newUser.getEmail());
            if (newUser.getRole() != null) existingUser.setRole(newUser.getRole());
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

    // Convert Entity to DTO
    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setNickname(user.getNickname());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    // Convert DTO to Entity
    public User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getUserId());
        user.setNickname(userDto.getNickname());
        user.setEmail(userDto.getEmail());
        return user;
    }

}
