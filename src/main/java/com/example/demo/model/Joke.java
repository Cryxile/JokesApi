package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Joke {
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
                        "\"targetLanguage\":\"%s\"}", id, type, originalSetup, originalPunchline, translatedSetup,
                translatedPunchline, originalLanguage, targetLanguage);
    }
}
