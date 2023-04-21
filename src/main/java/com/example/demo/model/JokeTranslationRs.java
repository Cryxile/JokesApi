package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JokeTranslationRs {

    private TranslatedData data;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TranslatedData {
        private Translations translations;

        @lombok.Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Translations {
            private String translatedText;
        }
    }
}
