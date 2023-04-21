package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OriginalAndTranslatedJoke {
    Integer id;
    String type;
    String originalSetup;
    String originalPunchline;
    String translatedSetup;
    String translatedPunchline;
    String originalLanguage;
    String targetLanguage;


    @Override
    public String toString() {
        return String.format("{\"id\":%d,\"type\":\"%s\",\"originalSetup\":\"%s\",\"originalPunchline\":\"%s\"," +
                        "\"translatedSetup\":\"%s\",\"translatedPunchline\":\"%s\",\"originalLanguage\":\"%s\"," +
                        "\"targetLanguage\":\"%s\"}" , id, type, originalSetup, originalPunchline, translatedSetup,
                translatedPunchline, originalLanguage, targetLanguage);
    }
}
