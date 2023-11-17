package com.example.demo.service;

import com.example.demo.model.Joke;

public interface EMailService {
    void send(Integer id);

    void send(Joke joke);
}
