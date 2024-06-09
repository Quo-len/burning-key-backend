package com.example.burningkey.user_lessons.service;

import com.example.burningkey.user_lessons.entity.UserLesson;
import com.example.burningkey.user_lessons.repository.UserLessonRepository;
import com.example.burningkey.user_sessions.entity.UserSession;
import com.example.burningkey.user_sessions.service.UserSessionService;
import com.example.burningkey.user_statistics.entity.UserStatistic;
import com.example.burningkey.user_statistics.service.UserStatisticService;
import com.example.burningkey.users.entity.User;
import com.example.burningkey.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UserLessonService {

    @Autowired
    private UserLessonRepository userLessonRepository;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserStatisticService userStatisticService;

    @Autowired
    private UserService userService;

    public long getUserLessonCount() { return userLessonRepository.count(); }

    public List<UserLesson> getUserLessons(Long userId, LocalDate date) {
        if (userId != null && date != null) {
            return userLessonRepository.findAllByUser_IdAndDate(userId, date);
        } else if (userId != null) {
            return userLessonRepository.findAllByUser_Id(userId);
        } else if (date != null) {
            return userLessonRepository.findAllByDate(date);
        } else {
            return userLessonRepository.findAll();
        }
    }

    public UserLesson createUserLesson(UserLesson userSession) {
        return userLessonRepository.save(userSession);
    }

    public void AddNewUserLesson(Long userId, UserLesson userLesson) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        User user = userOptional.get();
        userLesson.setUser(user);

        // update session of given day
        AtomicBoolean isNewSession = new AtomicBoolean(false);
        Optional<UserSession> userSessionOpt = Optional.ofNullable(userSessionService.getUserSessionByUserAndDate(user, userLesson.getDate()).orElseGet(() -> {
            UserSession newUserSession = userSessionService.createUserSession(user, userLesson.getDate());
            isNewSession.set(true);
            return newUserSession;
        }));
        UserSession userSession = userSessionOpt.get();

        userSession.setBestSpeedWpm(Math.max(userSession.getBestSpeedWpm(), userLesson.getAverageSpeedWpm()));
        userSession.setBestAccuracy(Math.max(userSession.getBestAccuracy(), userLesson.getAverageAccuracy()));

        double oldWpm = userSession.getAverageSpeedWpm();
        double oldAcc = userSession.getAverageAccuracy();

        userSession.setAverageSpeedWpm((userSession.getAverageSpeedWpm() * userSession.getNumLessons() + userLesson.getAverageSpeedWpm()) / (userSession.getNumLessons() + 1));
        userSession.setAverageAccuracy((userSession.getAverageAccuracy() * userSession.getNumLessons() + userLesson.getAverageAccuracy()) / (userSession.getNumLessons() + 1));

        userSession.setNumLessons(userSession.getNumLessons() + 1);
        userSession.setTimeSpent(userSession.getTimeSpent() + userLesson.getTimeSpent());

        userSessionService.updateUserSession(userSession.getId(), userSession);

        // update overall user statistics
        Optional<UserStatistic> userStatisticOpt = userStatisticService.getUserStatisticByUserId(user);
        UserStatistic userStatistic = userStatisticOpt.get();

        userStatistic.setBestSpeedWpm(Math.max(userStatistic.getBestSpeedWpm(), userLesson.getAverageSpeedWpm()));
        userStatistic.setBestAccuracy(Math.max(userStatistic.getBestAccuracy(), userLesson.getAverageAccuracy()));

        if(isNewSession.get()) {
            userStatistic.setAverageSpeedWpm((userStatistic.getAverageSpeedWpm() * userStatistic.getTotalSessions() + userSession.getAverageSpeedWpm()) / (userStatistic.getTotalSessions() + 1));
            userStatistic.setAverageAccuracy((userStatistic.getAverageAccuracy() * userStatistic.getTotalSessions() + userSession.getAverageAccuracy()) / (userStatistic.getTotalSessions() + 1));
        }
        else {
            userStatistic.setAverageSpeedWpm((userStatistic.getAverageSpeedWpm() * userStatistic.getTotalSessions() - oldWpm + userSession.getAverageSpeedWpm()) / (userStatistic.getTotalSessions()));
            userStatistic.setAverageAccuracy((userStatistic.getAverageAccuracy() * userStatistic.getTotalSessions() - oldAcc + userSession.getAverageAccuracy()) / (userStatistic.getTotalSessions()));
        }

        userStatistic.setTotalLessons(userStatistic.getTotalLessons() + 1);
        userStatistic.setTotalTimeSpent(userStatistic.getTotalTimeSpent() + userLesson.getTimeSpent());

        if(isNewSession.get())
            userStatistic.setTotalSessions(userStatistic.getTotalSessions() + 1);

        userStatisticService.updateUserStatistic(userStatistic.getId(), userStatistic);

        createUserLesson(userLesson);
    }

    public Optional<UserLesson> updateUserSession(Long id, UserLesson newUserLesson) {
        return userLessonRepository.findById(id).map(existingText -> {
            if (newUserLesson.getDate() != null) existingText.setDate(newUserLesson.getDate());
            if (newUserLesson.getTimeSpent() != null) existingText.setTimeSpent(newUserLesson.getTimeSpent());
            if (newUserLesson.getAverageSpeedWpm() != null) existingText.setAverageSpeedWpm(newUserLesson.getAverageSpeedWpm());
            if (newUserLesson.getAverageAccuracy() != null) existingText.setAverageAccuracy(newUserLesson.getAverageAccuracy());
            return userLessonRepository.save(existingText);
        });
    }

    public boolean deleteUserLesson(Long id) {
        if (!userLessonRepository.existsById(id)) {
            return false;
        }
        userLessonRepository.deleteById(id);
        return true;
    }
}
