package com.example.burningkey.user_sessions.entity;

import com.example.burningkey.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "user_sessions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private User user;

    private LocalDate date;

    private Integer numSessions;
    private LocalTime timeSpent;

    private Double bestSpeedWpm;
    private Double bestAccuracy;

    private Double averageSpeedWpm;
    private Double averageAccuracy;
}
