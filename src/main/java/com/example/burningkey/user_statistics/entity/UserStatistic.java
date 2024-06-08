package com.example.burningkey.user_statistics.entity;

import jakarta.persistence.*;
import lombok.*;

import com.example.burningkey.users.entity.User;

@Entity(name = "user_statistics")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserStatistic {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private Integer totalSessions = 0;
    private Integer totalLessons = 0;
    private Long totalTimeSpent = 0L;

    private Double bestSpeedWpm = 0.0;
    private Double bestAccuracy = 0.0;

    private Double averageSpeedWpm = 0.0;
    private Double averageAccuracy = 0.0;
}