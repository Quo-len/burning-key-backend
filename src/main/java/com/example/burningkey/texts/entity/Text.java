package com.example.burningkey.texts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "texts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;
    private String language;
    private String difficulty;

}
