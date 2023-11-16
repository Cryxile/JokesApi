package com.example.demo.model;

import com.example.demo.dto.JokeDTO;
import com.example.demo.model.db.JokeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Joke {
    public Joke(JokeEntity joke) {
        id = joke.getId();
        type = joke.getType();
        originalSetup = joke.getOriginalSetup();
        originalPunchline = joke.getOriginalPunchline();
        translatedSetup = joke.getTranslatedSetup();
        translatedPunchline = joke.getTranslatedPunchline();
        originalLanguage = joke.getOriginalLanguage();
        targetLanguage = joke.getTargetLanguage();
    }
    public Joke(JokeDTO joke) {
        id = joke.getId();
        type = joke.getType();
        originalSetup = joke.getOriginalSetup();
        originalPunchline = joke.getOriginalPunchline();
        translatedSetup = joke.getTranslatedSetup();
        translatedPunchline = joke.getTranslatedPunchline();
        originalLanguage = joke.getOriginalLanguage();
        targetLanguage = joke.getTargetLanguage();
    }
    private Integer id;
    private String type;
    private String originalSetup;
    private String originalPunchline;
    private String translatedSetup;
    private String translatedPunchline;
    private String originalLanguage;
    private String targetLanguage;


    @Override
    public String toString() {
        return String.format("{\"id\":%d,\"type\":\"%s\",\"originalSetup\":\"%s\",\"originalPunchline\":\"%s\"," +
                        "\"translatedSetup\":\"%s\",\"translatedPunchline\":\"%s\",\"originalLanguage\":\"%s\"," +
                        "\"targetLanguage\":\"%s\"}" , id, type, originalSetup, originalPunchline, translatedSetup,
                translatedPunchline, originalLanguage, targetLanguage);
    }
}
