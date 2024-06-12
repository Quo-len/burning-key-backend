package com.example.burningkey.user_lessons.entity;

import com.example.burningkey.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "user_lessons")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date;

    private Long timeSpent;

    private Double averageSpeedWpm;
    private Double averageAccuracy;

    public UserLesson(User user, LocalDate date, Long timeSpent, Double averageSpeedWpm, Double averageAccuracy) {
        this.user = user;
        this.date = date;
        this.timeSpent = timeSpent;
        this.averageSpeedWpm = averageSpeedWpm;
        this.averageAccuracy = averageAccuracy;
    }
}
