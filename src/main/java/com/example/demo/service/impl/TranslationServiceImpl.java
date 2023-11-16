package com.example.demo.service.impl;

import com.example.demo.dto.JokeTranslationRqDTO;
import com.example.demo.model.JokeTranslationRs;
import com.example.demo.service.TranslationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.example.demo.utils.Constants.*;

@Service
public class TranslationServiceImpl implements TranslationService {

    private final RestTemplate restTemplate;
    private final String rapidKey;
    private final String rapidHost;
    private final String translationURL;
    private final String targetLang;

    public TranslationServiceImpl(RestTemplate restTemplate,
                                  @Value("${translation-service.rapid-key}") String rapidKey,
                                  @Value("${translation-service.rapid-host}") String rapidHost,
                                  @Value("${translation-service.url}") String translationURL,
                                  @Value("${translation-service.target-language}") String targetLang) {
        this.restTemplate = restTemplate;
        this.rapidKey = rapidKey;
        this.rapidHost = rapidHost;
        this.translationURL = translationURL;
        this.targetLang = targetLang;
    }

    @Override
    public String translateString(String jokePart) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(RAPID_KEY, rapidKey);
        headers.add(RAPID_HOST, rapidHost);
        JokeTranslationRqDTO jokeTranslationRqDTO = new JokeTranslationRqDTO(
                jokePart,
                ORIGINAL_LANGUAGE,
                targetLang
        );
        HttpEntity<JokeTranslationRqDTO> request = new HttpEntity<>(jokeTranslationRqDTO, headers);
        JokeTranslationRs response = restTemplate.postForObject(
                translationURL,
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


