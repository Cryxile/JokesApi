package com.example.demo.service;

import com.example.demo.model.OriginalAndTranslatedJoke;

import java.util.List;

public interface JokeService {
    void addJoke(String type, String setup, String punchline, Integer id);

    void deleteJoke(Integer id);

    void editJoke(String type, String setup, String punchline, Integer id);

    List<OriginalAndTranslatedJoke> getJokeList();

    OriginalAndTranslatedJoke getJoke(Integer id);

    OriginalAndTranslatedJoke getRandomJoke();
}
