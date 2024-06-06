package com.example.burningkey.user_lessons.entity;

import com.example.burningkey.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    private LocalDateTime date;

    private Long timeSpent;

    private Double averageSpeedWpm;
    private Double averageAccuracy;
}
