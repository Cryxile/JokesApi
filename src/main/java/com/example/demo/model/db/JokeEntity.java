package com.example.demo.model.db;

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
