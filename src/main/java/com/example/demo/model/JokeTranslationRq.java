package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JokeTranslationRq {
    String q;
    String source;
    String target;
}
