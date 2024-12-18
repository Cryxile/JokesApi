package com.example.demo.service.impl;

import com.example.demo.dto.RawJokeDTO;
import com.example.demo.mapper.JokeMapper;
import com.example.demo.model.Joke;
import com.example.demo.model.RawJoke;
import com.example.demo.repository.JokeRepository;
import com.example.demo.service.EMailService;
import com.example.demo.service.JokeService;
import com.example.demo.service.TranslationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.constants.Constants.ORIGINAL_LANGUAGE;


@Service
public class JokeServiceImpl implements JokeService {
    private final RestTemplate restTemplate;
    private final TranslationService translationService;
    private final JokeMapper jokeMapper;
    private final JokeRepository jokeRepository;
    private final EMailService eMailService;
    private final String targetLang;
    private final String resourceURL;

    public JokeServiceImpl(RestTemplate restTemplate,
                           TranslationService translationService,
                           JokeMapper jokeMapper,
                           JokeRepository jokeRepository,
                           EMailService eMailService,
                           @Value("${translation-service.target-language}") String targetLang,
                           @Value("${joke-service.url}") String resourceURL) {
        this.restTemplate = restTemplate;
        this.translationService = translationService;
        this.jokeMapper = jokeMapper;
        this.jokeRepository = jokeRepository;
        this.eMailService = eMailService;
        this.targetLang = targetLang;
        this.resourceURL = resourceURL;
    }

    @Override
    public void addJoke(RawJoke rawJoke) {
        Joke joke = new Joke(
                rawJoke.getId(),
                translationService.translateString(rawJoke.getType()),
                rawJoke.getSetup(),
                rawJoke.getPunchline(),
                translationService.translateString(rawJoke.getSetup()),
                translationService.translateString(rawJoke.getPunchline()),
                ORIGINAL_LANGUAGE,
                targetLang
        );
        jokeRepository.save(jokeMapper.mapJokeToJokeEntity(joke));
    }

    @Override
    public void deleteJoke(Integer id) {
        jokeRepository.deleteById(id);
    }

    @Override
    public void editJoke(Joke joke) {
        jokeRepository.save(jokeMapper.mapJokeToJokeEntity(joke));
    }

    @Override
    public List<Joke> getJokeList() {
        return jokeRepository.findAll().stream()
                .map(jokeMapper::mapJokeEntityToJoke)
                .collect(Collectors.toList());
    }

    @Override
    public Joke getJoke(Integer id) {
        return jokeMapper.mapJokeEntityToJoke(jokeRepository.getReferenceById(id));
    }

    @Override
    public Joke getRandomJoke() {
        ResponseEntity<RawJokeDTO> response = restTemplate.getForEntity(resourceURL, RawJokeDTO.class);
        RawJokeDTO receivedJoke = response.getBody();
        if (response.getStatusCode().equals(HttpStatus.OK) && receivedJoke != null) {
            Joke joke = new Joke(
                    receivedJoke.getId(),
                    translationService.translateString(receivedJoke.getType()),
                    receivedJoke.getSetup(),
                    receivedJoke.getPunchline(),
                    translationService.translateString(receivedJoke.getSetup()),
                    translationService.translateString(receivedJoke.getPunchline()),
                    ORIGINAL_LANGUAGE,
                    targetLang
            );
            jokeRepository.save(jokeMapper.mapJokeToJokeEntity(joke));
            return joke;
        } else {
            return null;
        }
    }

    @Override
    public void send(Integer id) {
        eMailService.send(id);
    }

    @Override
    public void send(Joke joke) {
        eMailService.send(getRandomJoke());
    }
}
