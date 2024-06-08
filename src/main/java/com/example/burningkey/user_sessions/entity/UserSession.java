package com.example.burningkey.user_sessions.entity;

import com.example.burningkey.users.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    private Integer numLessons = 0;
    private Long timeSpent = 0L;

    private Double bestSpeedWpm = 0.0;
    private Double bestAccuracy = 0.0;

    private Double averageSpeedWpm = 0.0;
    private Double averageAccuracy = 0.0;

}
