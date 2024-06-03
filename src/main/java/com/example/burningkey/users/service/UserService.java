package com.example.burningkey.users.service;

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
                .username("admin")
                .role(Role.ADMIN)
                .build());

        userRepository.save(User.builder()
                .email("user@example.com")
                .username("User1")
                .role(Role.USER)
                .build());

        userRepository.save(User.builder()
                .email("user2@example.com")
                .username("User2")
                .role(Role.USER)
                .build());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User newUser) {
        return userRepository.findById(id).map(existingUser -> {
            if (newUser.getUsername() != null) existingUser.setUsername(newUser.getUsername());
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

}
