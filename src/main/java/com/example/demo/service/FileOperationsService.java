package com.example.demo.service;


import com.example.demo.model.Joke;

public interface FileOperationsService {

    void writeJoke(Joke joke);

    void removeJoke(int id);

    Joke getJoke(Integer id);

}
