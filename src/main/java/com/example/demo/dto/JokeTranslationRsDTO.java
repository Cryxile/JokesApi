package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JokeTranslationRsDTO {

    class Data {
        private List<Translations> translations;

        class Translations {
            private String translatedText;
        }
    }
}
