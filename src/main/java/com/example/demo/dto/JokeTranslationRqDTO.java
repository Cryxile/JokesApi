package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JokeTranslationRqDTO {

    String q;
    String source;
    String target;

}
