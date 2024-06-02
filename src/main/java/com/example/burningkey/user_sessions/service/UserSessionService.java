package com.example.burningkey.user_sessions.service;

import com.example.burningkey.user_sessions.entity.UserSession;
import com.example.burningkey.user_sessions.repository.UserSessionRepository;
import com.example.burningkey.users.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    public long getUserSessionCount() { return userSessionRepository.count(); }

    public List<UserSession> getUserSessions(User user, LocalDate date) {
        if (user != null && date != null) {
            return userSessionRepository.findUserSessionByUserAndDate(user, date);
        } else if (user != null) {
            return userSessionRepository.findUserSessionByUser(user);
        } else if (date != null) {
            return userSessionRepository.findUserSessionByDate(date);
        } else {
            return userSessionRepository.findAll();
        }
    }

    public UserSession createUserSession(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }

    public Optional<UserSession> updateUserSession(Long id, UserSession newUserSession) {
        return userSessionRepository.findById(id).map(existingText -> {
            if (newUserSession.getDate() != null) existingText.setDate(newUserSession.getDate());
            if (newUserSession.getNumSessions() != null) existingText.setNumSessions(newUserSession.getNumSessions());
            if (newUserSession.getTimeSpent() != null) existingText.setTimeSpent(newUserSession.getTimeSpent());
            if (newUserSession.getAverageSpeedWpm() != null) existingText.setAverageSpeedWpm(newUserSession.getAverageSpeedWpm());
            if (newUserSession.getAverageAccuracy() != null) existingText.setAverageAccuracy(newUserSession.getAverageAccuracy());
            if (newUserSession.getBestSpeedWpm() != null) existingText.setBestSpeedWpm(newUserSession.getBestSpeedWpm());
            if (newUserSession.getBestAccuracy() != null) existingText.setBestAccuracy(newUserSession.getBestAccuracy());
            return userSessionRepository.save(existingText);
        });
    }

    public boolean deleteUserSession(Long id) {
        if (!userSessionRepository.existsById(id)) {
            return false;
        }
        userSessionRepository.deleteById(id);
        return true;
    }


}
