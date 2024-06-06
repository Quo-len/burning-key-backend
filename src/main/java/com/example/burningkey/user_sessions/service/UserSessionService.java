package com.example.burningkey.user_sessions.service;

import com.example.burningkey.user_lessons.repository.UserLessonRepository;
import com.example.burningkey.user_sessions.entity.UserSession;
import com.example.burningkey.user_sessions.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private UserLessonRepository userLessonRepository;

    public long getUserSessionCount() { return userSessionRepository.count(); }

    public List<UserSession> getUserSessions(Long userId, LocalDate date) {
        if (userId != null) {
            return userSessionRepository.findAllByUser_Id(userId);
        } else if (date != null) {
            return userSessionRepository.findAllByDate(date);
        } else {
            return userSessionRepository.findAll();
        }
    }

    public Optional<UserSession> findUserSessionByUserIdAndDate(Long userId, LocalDate date) {
        return userSessionRepository.findByUser_IdAndDate(userId, date);
    }

    public UserSession createUserSession(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }

   /* public UserSession getStatsForDayBrute(User user, LocalDate date) {
        List<UserLesson> lessons = userLessonRepository.findAllByUserAndDate(user, date);

        return
    }*/

    public Optional<UserSession> updateUserSession(Long id, UserSession newUserSession) {
        return userSessionRepository.findById(id).map(existingText -> {
            if (newUserSession.getDate() != null) existingText.setDate(newUserSession.getDate());
            if (newUserSession.getNumLessons() != null) existingText.setNumLessons(newUserSession.getNumLessons());
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
