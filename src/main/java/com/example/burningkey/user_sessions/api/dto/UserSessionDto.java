package com.example.burningkey.user_sessions.api.dto;

import com.example.burningkey.users.api.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSessionDto {
    private Long id;

    private UserDto userDto;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    private Integer numLessons;
    private Long timeSpent;

    private Double bestSpeedWpm;
    private Double bestAccuracy;

    private Double averageSpeedWpm;
    private Double averageAccuracy;
}
