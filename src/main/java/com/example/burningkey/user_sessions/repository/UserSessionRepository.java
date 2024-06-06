package com.example.burningkey.user_sessions.repository;

import com.example.burningkey.user_sessions.entity.UserSession;
import com.example.burningkey.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long>  {

    List<UserSession> findAllByUser_Id(Long userId);
    List<UserSession> findAllByDate(LocalDate date); // get all sessions of all users for leader board of particular day
    Optional<UserSession> findByUser_IdAndDate(Long userId, LocalDate date);

}
