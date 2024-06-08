package com.example.burningkey.user_statistics.api.dto;

import com.example.burningkey.users.api.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserStatisticDto {
    private Long id;

    private UserDto userDto;

    private Integer totalSessions;
    private Integer totalLessons;
    private Long totalTimeSpent;

    private Double bestSpeedWpm;
    private Double bestAccuracy;

    private Double averageSpeedWpm;
    private Double averageAccuracy;
}
