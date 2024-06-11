package com.example.burningkey.user_statistics.service;

import com.example.burningkey.user_statistics.entity.UserStatistic;
import com.example.burningkey.user_statistics.repository.UserStatisticRepository;
import com.example.burningkey.users.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStatisticService {

    @Autowired
    private UserStatisticRepository userStatisticRepository;

    public long getUserStatisticCount() { return userStatisticRepository.count(); }

    public List<UserStatistic> getAllUserStatistics() {
        return userStatisticRepository.findAll();
    }

    public Optional<UserStatistic> getUserStatisticById(Long id) {
        return userStatisticRepository.findById(id);
    }

    public Optional<UserStatistic> getUserStatisticByUserId(User user) {
        return Optional.ofNullable(userStatisticRepository.findByUser_Id(user.getId()).orElseGet(() -> createUserStatistic(user)));
    }

    public UserStatistic createUserStatistic(User user) {
        UserStatistic newUserSession = new UserStatistic();
        newUserSession.setUser(user);
        return userStatisticRepository.save(newUserSession);
    }

    public Optional<UserStatistic> updateUserStatistic(Long id, UserStatistic newUserStatistic) {
        return userStatisticRepository.findById(id).map(existingStatistic -> {
            if (newUserStatistic.getUser() != null) existingStatistic.setUser(newUserStatistic.getUser());
            if (newUserStatistic.getTotalSessions() != null) existingStatistic.setTotalSessions(newUserStatistic.getTotalSessions());
            if (newUserStatistic.getTotalTimeSpent() != null) existingStatistic.setTotalTimeSpent(newUserStatistic.getTotalTimeSpent());
            if (newUserStatistic.getBestSpeedWpm() != null) existingStatistic.setBestSpeedWpm(newUserStatistic.getBestSpeedWpm());
            if (newUserStatistic.getBestAccuracy() != null) existingStatistic.setBestAccuracy(newUserStatistic.getBestAccuracy());
            if (newUserStatistic.getAverageSpeedWpm() != null) existingStatistic.setAverageSpeedWpm(newUserStatistic.getAverageSpeedWpm());
            if (newUserStatistic.getAverageAccuracy() != null) existingStatistic.setAverageAccuracy(newUserStatistic.getAverageAccuracy());
            return userStatisticRepository.save(existingStatistic);
        });
    }

    public boolean deleteUserStatistic(Long id) {
        if (!userStatisticRepository.existsById(id)) {
            return false;
        }
        userStatisticRepository.deleteById(id);
        return true;
    }

    public List<UserStatistic> getTop1000ByBestWpmAndAccuracy() {
        return userStatisticRepository.findTop100ByBestWpmAndAccuracy();
    }

}
