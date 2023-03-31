package com.example.demo.service;

import com.example.demo.model.Joke;

import java.util.List;

public interface JokeService {
    void addJoke(String type, String setup, String punchline, Integer id);

    void addJoke(Joke joke);

    void deleteJoke(Integer id);

    void editJoke(String type, String setup, String punchline, Integer id);

    List<Joke> getJokeList();

    Joke getJoke(Integer id);

    List<Joke> getRandomJoke();
}
