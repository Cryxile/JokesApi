package com.example.demo.service.impl;

import com.example.demo.dto.JokeDTO;
import com.example.demo.model.Joke;
import com.example.demo.model.OriginalAndTranslatedJoke;
import com.example.demo.service.FileOperationsService;
import com.example.demo.service.JokeService;
import com.example.demo.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.utils.Constants.*;


@RequiredArgsConstructor
@Service
public class JokeServiceImpl implements JokeService {

    private final RestTemplate restTemplate;
    private final List<OriginalAndTranslatedJoke> origAndTranslJokes = new ArrayList<>();
    private final FileOperationsService fileWritingService;
    private final TranslationService translationService;



    @Override
    public void addJoke(String type, String setup, String punchline, Integer id) {
        if (origAndTranslJokes.stream().noneMatch(e -> e.getId().equals(id))) {
            OriginalAndTranslatedJoke originalAndTranslatedJoke = new OriginalAndTranslatedJoke(
                    id,
                    type,
                    setup,
                    punchline,
                    setup,
                    punchline,
                    ORIGINAL_LANGUAGE,
                    TARGET_LANGUAGE
            );
//        if (origAndTranslJokes.stream().noneMatch(e -> e.getId().equals(id))) {
//            OriginalAndTranslatedJoke originalAndTranslatedJoke = new OriginalAndTranslatedJoke(
//                    id,
//                    translationService.translateString(type),
//                    setup,
//                    punchline,
//                    translationService.translateString(setup),
//                    translationService.translateString(punchline),
//                    ORIGINAL_LANGUAGE,
//                    TARGET_LANGUAGE
//            );
            Integer prevListLength = origAndTranslJokes.size();
            origAndTranslJokes.add(originalAndTranslatedJoke);
            fileWritingService.writeToFile(origAndTranslJokes, prevListLength);
        }
    }

    @Override
    public void deleteJoke(Integer id) {
        //firstListLength = origAndTranslJokes.size();
        origAndTranslJokes.remove(getJoke(id));
        fileWritingService.removeFromFile(id);
    }

    @Override
    public void editJoke(String type, String setup, String punchline, Integer id) {
        OriginalAndTranslatedJoke joke = getJoke(id);
        joke.setType(type);
        joke.setOriginalSetup(setup);
        joke.setTranslatedSetup(translationService.translateString(setup));
        joke.setOriginalPunchline(punchline);
        joke.setTranslatedPunchline(translationService.translateString(punchline));
    }

    @Override
    public List<OriginalAndTranslatedJoke> getJokeList() {
        return origAndTranslJokes;
    }

    @Override
    public OriginalAndTranslatedJoke getJoke(Integer id) {
        return origAndTranslJokes.stream()
                .filter(e ->
                        e.getId().equals(id)
                )
                .findFirst()
                .orElse(null);
    }

    @Override
    public OriginalAndTranslatedJoke getRandomJoke() {
        ResponseEntity<JokeDTO> response = restTemplate.getForEntity(RESOURCE_URL, JokeDTO.class);
        if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
            JokeDTO receivedJoke = response.getBody();
            Joke joke = new Joke(
                    receivedJoke.getType(),
                    receivedJoke.getSetup(),
                    receivedJoke.getPunchline(),
                    receivedJoke.getId()
            );
            Joke translatedJoke = new Joke(
                    translationService.translateString(receivedJoke.getType()),
                    translationService.translateString(receivedJoke.getSetup()),
                    translationService.translateString(receivedJoke.getPunchline()),
                    receivedJoke.getId()
            );
            OriginalAndTranslatedJoke originalAndTranslatedJoke = new OriginalAndTranslatedJoke(
                    joke.getId(),
                    translatedJoke.getType(),
                    joke.getSetup(),
                    joke.getPunchline(),
                    translatedJoke.getSetup(),
                    translatedJoke.getPunchline(),
                    ORIGINAL_LANGUAGE,
                    TARGET_LANGUAGE
            );
            Integer prevListLength = origAndTranslJokes.size();
            origAndTranslJokes.add(originalAndTranslatedJoke);
            fileWritingService.writeToFile(origAndTranslJokes, prevListLength);
            return originalAndTranslatedJoke;
        } else {
            return null;
        }
    }
}
