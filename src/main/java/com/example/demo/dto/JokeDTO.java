package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JokeDTO {
    Integer id;
    String type;
    String originalSetup;
    String originalPunchline;
    String translatedSetup;
    String translatedPunchline;
    String originalLanguage;
    String targetLanguage;

}
