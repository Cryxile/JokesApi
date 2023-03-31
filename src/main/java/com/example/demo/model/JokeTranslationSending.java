package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JokeTranslationSending {

    String q;
    String source;
    String target;

}
