package com.example.burningkey.texts.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextDto {
    private Long textId;
    private String title;
    private String content;
    private String language;
    private String difficulty;
}
