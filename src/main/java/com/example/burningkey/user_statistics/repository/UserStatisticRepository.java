package com.example.burningkey.user_statistics.repository;

import com.example.burningkey.user_statistics.entity.UserStatistic;
import com.example.burningkey.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatisticRepository extends JpaRepository<UserStatistic, Long> {

    Optional<UserStatistic> findByUser(User user);
}
