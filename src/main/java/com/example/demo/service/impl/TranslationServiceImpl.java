package com.example.demo.service.impl;

import com.example.demo.model.TranslationRq;
import com.example.demo.model.TranslationRs;
import com.example.demo.service.TranslationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.example.demo.constants.Constants.*;

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
        HttpEntity<TranslationRq> request = new HttpEntity<>(
                new TranslationRq(
                        jokePart,
                        ORIGINAL_LANGUAGE,
                        targetLang
                ),
                headers
        );
        TranslationRs response = restTemplate.postForObject(
                translationURL,
                request,
                TranslationRs.class
        );
        if (response != null) {
            return response.getData().getTranslations().getTranslatedText();
        } else {
            return null;
        }
    }
}
