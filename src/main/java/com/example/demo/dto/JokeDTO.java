package com.example.demo.dto;

import com.example.demo.model.Joke;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JokeDTO {

    public JokeDTO(Joke joke) {
        id = joke.getId();
        type = joke.getType();
        originalSetup = joke.getOriginalSetup();
        originalPunchline = joke.getOriginalPunchline();
        translatedSetup = joke.getTranslatedSetup();
        translatedPunchline = joke.getTranslatedPunchline();
        originalLanguage = joke.getOriginalLanguage();
        targetLanguage = joke.getTargetLanguage();
    }
    Integer id;
    String type;
    String originalSetup;
    String originalPunchline;
    String translatedSetup;
    String translatedPunchline;
    String originalLanguage;
    String targetLanguage;

}
