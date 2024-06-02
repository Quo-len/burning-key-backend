package com.example.burningkey.user_sessions.repository;

import com.example.burningkey.user_sessions.entity.UserSession;
import com.example.burningkey.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserSessionRepository extends JpaRepository<UserSession, Long>  {

    List<UserSession> findUserSessionByUser(User user);
    List<UserSession> findUserSessionByDate(LocalDate date);
    List<UserSession> findUserSessionByUserAndDate(User user, LocalDate date);
}
