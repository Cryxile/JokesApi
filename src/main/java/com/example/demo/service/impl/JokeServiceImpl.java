package com.example.demo.service.impl;

import com.example.demo.dto.AddJokeDTO;
import com.example.demo.dto.JokeTranslationRqDTO;
import com.example.demo.model.Joke;
import com.example.demo.model.JokeTranslationGetting;
import com.example.demo.service.JokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JokeServiceImpl implements JokeService {

    private static final String RESOURCE_URL = "https://official-joke-api.appspot.com/random_joke";
    private static final String TRANSLATION_URL = "https://deep-translate1.p.rapidapi.com/language/translate/v2";
    private final List<Joke> originalJokes;
    private final List<Joke> translatedJokes;
    private final RestTemplate restTemplate;


    @Override
    public void addJoke(String type, String setup, String punchline, Integer id) {
        Joke joke = new Joke(type, setup, punchline, id);
        originalJokes.add(joke);
    }

    @Override
    public void addJoke(Joke joke) {
        originalJokes.add(joke);
    }

    @Override
    public void deleteJoke(Integer id) {
        originalJokes.remove(getJoke(id));
    }

    @Override
    public void editJoke(String type, String setup, String punchline, Integer id) {
        Joke joke = getJoke(id);
        joke.setType(type);
        joke.setSetup(setup);
        joke.setPunchline(punchline);
    }

    @Override
    public List<Joke> getJokeList() {
        return originalJokes;
    }

    @Override
    public Joke getJoke(Integer id) {
        return originalJokes.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Joke> getRandomJoke() {
        ResponseEntity<AddJokeDTO> response = restTemplate.getForEntity(RESOURCE_URL, AddJokeDTO.class);
        if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
            List<Joke> origAndTranslJokes = new ArrayList<>();
            AddJokeDTO receivedJoke = response.getBody();
            Joke joke = new Joke(receivedJoke.getType(), receivedJoke.getSetup(), receivedJoke.getPunchline(),
                    receivedJoke.getId());
            originalJokes.add(joke);
            Joke translatedJoke = new Joke(translateJoke(receivedJoke.getType()), translateJoke(receivedJoke.getSetup()),
                    translateJoke(receivedJoke.getPunchline()), receivedJoke.getId());
            translatedJokes.add(translatedJoke);
            origAndTranslJokes.add(joke);
            origAndTranslJokes.add(translatedJoke);
            return origAndTranslJokes;
        } else {
            return null;
        }
    }

    private String translateJoke(String jokePart) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-RapidAPI-Key", "70e8b7f1aamsha82487dfb0190b6p13ccc7jsn2afff2bb4a75");
        headers.add("X-RapidAPI-Host", "deep-translate1.p.rapidapi.com");

        JokeTranslationRqDTO jokeTranslationRqDTO = new JokeTranslationRqDTO(jokePart, "en", "ru");
        HttpEntity<JokeTranslationRqDTO> request = new HttpEntity<>(jokeTranslationRqDTO, headers);

        JokeTranslationGetting response = restTemplate.postForObject(TRANSLATION_URL, request,
                JokeTranslationGetting.class);
        if (response != null) {                                                                     //null check
            return response.getData().getTranslations().getTranslatedText();
        } else {
            return null;
        }
    }
}
