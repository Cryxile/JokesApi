package com.example.demo.service;


import com.example.demo.model.OriginalAndTranslatedJoke;

import java.util.List;

public interface FileOperationsService {

    void writeToFile(List<OriginalAndTranslatedJoke> origAndTranslJokes, Integer firstListLength);

    void removeFromFile(Integer id);

}
