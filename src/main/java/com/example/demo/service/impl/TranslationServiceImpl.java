package com.example.demo.service.impl;

import com.example.demo.dto.JokeTranslationRqDTO;
import com.example.demo.model.JokeTranslationRs;
import com.example.demo.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.example.demo.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final RestTemplate restTemplate;

    @Override
    public String translateString(String jokePart) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-RapidAPI-Key", "70e8b7f1aamsha82487dfb0190b6p13ccc7jsn2afff2bb4a75");
        headers.add("X-RapidAPI-Host", "deep-translate1.p.rapidapi.com");

        JokeTranslationRqDTO jokeTranslationRqDTO = new JokeTranslationRqDTO(jokePart,
                ORIGINAL_LANGUAGE,
                TARGET_LANGUAGE
        );
        HttpEntity<JokeTranslationRqDTO> request = new HttpEntity<>(jokeTranslationRqDTO, headers);

        JokeTranslationRs response = restTemplate.postForObject(TRANSLATION_URL,
                request,
                JokeTranslationRs.class
        );
        if (response != null) {
            return response.getData().getTranslations().getTranslatedText();
        } else {
            return null;
        }
    }



}


