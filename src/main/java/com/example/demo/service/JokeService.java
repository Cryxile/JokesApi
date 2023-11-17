package com.example.demo.service;

import com.example.demo.model.Joke;
import com.example.demo.model.RawJoke;

import java.util.List;

public interface JokeService {
    void addJoke(RawJoke rawJoke);

    void deleteJoke(Integer id);

    void editJoke(Joke joke);

    List<Joke> getJokeList();

    Joke getJoke(Integer id);

    Joke getRandomJoke();

    void send(Integer id);

    void send(Joke joke);
}
