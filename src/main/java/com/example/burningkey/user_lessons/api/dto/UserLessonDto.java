package com.example.burningkey.user_lessons.api.dto;

import com.example.burningkey.users.api.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLessonDto {
    private Long id;
    private UserDto userDto;
    private LocalDateTime date;
    private Long timeSpent;
    private Double averageSpeedWpm;
    private Double averageAccuracy;
}
