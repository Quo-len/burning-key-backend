package com.example.burningkey.user_statistics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

import com.example.burningkey.users.entity.User;

@Entity(name = "user_statistics")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userStatisticId;

    @OneToOne // wtf is this relation
    @MapsId
    @JoinColumn(name = "userId")
    private User user;

    private Integer totalSessions;
    private LocalTime totalTimeSpent;

    private Double bestSpeedWpm;
    private Double bestAccuracy;

    private Double averageSpeedWpm;
    private Double averageAccuracy;
}