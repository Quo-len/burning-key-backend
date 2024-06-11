package com.example.burningkey.user_statistics.repository;

import com.example.burningkey.user_statistics.api.dto.LeaderboardEntryDto;
import com.example.burningkey.user_statistics.entity.UserStatistic;
import com.example.burningkey.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserStatisticRepository extends JpaRepository<UserStatistic, Long> {

    Optional<UserStatistic> findByUser_Id(Long userId);

    @Query("SELECT new com.example.burningkey.user_statistics.api.dto.LeaderboardEntryDto(us.user.nickname, us.averageSpeedWpm, us.averageAccuracy) FROM user_statistics us ORDER BY (us.averageSpeedWpm * us.averageAccuracy) DESC LIMIT 1000")
    List<LeaderboardEntryDto> findTop1000ByBestWpmAndAccuracy();
}
