package com.example.demo.model.db;

import com.example.demo.model.Joke;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "jokes")
public class JokeEntity {
    public JokeEntity(Joke joke) {
        id = joke.getId();
        type = joke.getType();
        originalSetup = joke.getOriginalSetup();
        originalPunchline = joke.getOriginalPunchline();
        translatedSetup = joke.getTranslatedSetup();
        translatedPunchline = joke.getTranslatedPunchline();
        originalLanguage = joke.getOriginalLanguage();
        targetLanguage = joke.getTargetLanguage();
    }
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "topic")
    private String type;
    @Column(name = "originalsetup")
    private String originalSetup;
    @Column(name = "originalpunchline")
    private String originalPunchline;
    @Column(name = "translatedsetup")
    private String translatedSetup;
    @Column(name = "translatedpunchline")
    private String translatedPunchline;
    @Column(name = "originallanguage")
    private String originalLanguage;
    @Column(name = "targetlanguage")
    private String targetLanguage;
}
