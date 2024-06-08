package com.example.burningkey.user_lessons.api.dto;

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
public class UserLessonDto {
    private Long id;
    private UserDto userDto;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;
    private Long timeSpent;
    private Double averageSpeedWpm;
    private Double averageAccuracy;
}
