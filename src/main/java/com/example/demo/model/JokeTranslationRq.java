package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

//TODO del?
@Data
@AllArgsConstructor
public class JokeTranslationRq {
    String q;
    String source;
    String target;
}
