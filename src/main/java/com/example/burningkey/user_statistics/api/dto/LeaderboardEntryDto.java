package com.example.burningkey.user_statistics.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaderboardEntryDto {
    private String nickname;
    private Double averageSpeedWpm;
    private Double averageAccuracy;
}
