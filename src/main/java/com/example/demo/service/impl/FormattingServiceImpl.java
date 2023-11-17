package com.example.demo.service.impl;

import com.example.demo.model.Joke;
import com.example.demo.service.FormattingService;
import org.springframework.stereotype.Service;

import static com.example.demo.constants.Constants.MESSAGE_FORMAT_TEMPLATE;

@Service
public class FormattingServiceImpl implements FormattingService {

    @Override
    public String beautifyJoke(Joke joke) {
        return String.format(
                MESSAGE_FORMAT_TEMPLATE,
                joke.getId(),
                joke.getType(),
                joke.getOriginalLanguage(),
                joke.getOriginalSetup(),
                joke.getOriginalPunchline(),
                joke.getTargetLanguage(),
                joke.getTranslatedSetup(),
                joke.getTranslatedPunchline()
        );
    }
}
