package com.example.burningkey.user_statistics.service;

import com.example.burningkey.user_statistics.entity.UserStatistic;
import com.example.burningkey.user_statistics.repository.UserStatisticRepository;
import com.example.burningkey.users.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserStatisticService {

    @Autowired
    private UserStatisticRepository userStatisticRepository;

    public long getUserStatisticCount() { return userStatisticRepository.count(); }

    public List<UserStatistic> getAllUserStatistics(User user, LocalDate date) {
        return userStatisticRepository.findAll();
    }

    public Optional<UserStatistic> getUserStatisticById(Long id) {
        return userStatisticRepository.findById(id);
    }

    public Optional<UserStatistic> getUserStatisticByUser(User user) {
        return userStatisticRepository.findByUser(user);
    }

    public UserStatistic createUserStatistic(UserStatistic userSession) {
        return userStatisticRepository.save(userSession);
    }

    public Optional<UserStatistic> updateUserStatistic(Long id, UserStatistic newUserSession) {
        return userStatisticRepository.findById(id).map(existingText -> {
            if (newUserSession.getUser() != null) existingText.setUser(newUserSession.getUser());
            if (newUserSession.getTotalSessions() != null) existingText.setTotalSessions(newUserSession.getTotalSessions());
            if (newUserSession.getTotalTimeSpent() != null) existingText.setTotalTimeSpent(newUserSession.getTotalTimeSpent());
            if (newUserSession.getBestSpeedWpm() != null) existingText.setBestSpeedWpm(newUserSession.getBestSpeedWpm());
            if (newUserSession.getBestAccuracy() != null) existingText.setBestAccuracy(newUserSession.getBestAccuracy());
            if (newUserSession.getAverageSpeedWpm() != null) existingText.setAverageSpeedWpm(newUserSession.getAverageSpeedWpm());
            if (newUserSession.getAverageAccuracy() != null) existingText.setAverageAccuracy(newUserSession.getAverageAccuracy());
            return userStatisticRepository.save(existingText);
        });
    }

    public boolean deleteUserStatistic(Long id) {
        if (!userStatisticRepository.existsById(id)) {
            return false;
        }
        userStatisticRepository.deleteById(id);
        return true;
    }


}
