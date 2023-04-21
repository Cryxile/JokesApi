package com.example.demo.controller;

import com.example.demo.dto.JokeDTO;
import com.example.demo.dto.OriginalAndTranslatedJokeDTO;
import com.example.demo.model.OriginalAndTranslatedJoke;
import com.example.demo.service.JokeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class JokeController {

    private final JokeService jokeService;

    @GetMapping("/joke/random-joke")
    public OriginalAndTranslatedJokeDTO getRandomJoke() {
        OriginalAndTranslatedJoke oAndTJoke = jokeService.getRandomJoke();
        return new OriginalAndTranslatedJokeDTO(
                oAndTJoke.getId(),
                oAndTJoke.getType(),
                oAndTJoke.getOriginalSetup(),
                oAndTJoke.getOriginalPunchline(),
                oAndTJoke.getTranslatedSetup(),
                oAndTJoke.getTranslatedPunchline(),
                oAndTJoke.getOriginalLanguage(),
                oAndTJoke.getTargetLanguage()
        );
    }

    @GetMapping("/joke")
    public List<OriginalAndTranslatedJoke> getJokeList() {
        return jokeService.getJokeList();
    }

    @PutMapping("/joke/add")
    public ResponseEntity<String> addJoke(@RequestBody JokeDTO jokeDTO) {
        jokeService.addJoke(
                jokeDTO.getType(),
                jokeDTO.getSetup(),
                jokeDTO.getPunchline(),
                jokeDTO.getId()
        );
        return new ResponseEntity<>("Joke successfully added!", HttpStatus.OK);
    }

    @DeleteMapping("/joke/{id}")
    public ResponseEntity<String> deleteJoke(@PathVariable("id") Integer id) {
        jokeService.deleteJoke(id);
        return new ResponseEntity<>("Joke successfully deleted!", HttpStatus.OK);
    }

    @PostMapping("/joke")
    public ResponseEntity<String> editJoke(@RequestBody JokeDTO jokeDTO) {
        jokeService.editJoke(
                jokeDTO.getType(),
                jokeDTO.getSetup(),
                jokeDTO.getPunchline(),
                jokeDTO.getId()
        );
        return new ResponseEntity<>("Joke successfully edited!", HttpStatus.OK);
    }


    @GetMapping("/joke/{id}")
    public OriginalAndTranslatedJokeDTO getJoke(@PathVariable("id") Integer id) {
        OriginalAndTranslatedJoke oAndTJoke = jokeService.getJoke(id);
        return new OriginalAndTranslatedJokeDTO(
                oAndTJoke.getId(),
                oAndTJoke.getType(),
                oAndTJoke.getOriginalSetup(),
                oAndTJoke.getOriginalPunchline(),
                oAndTJoke.getTranslatedSetup(),
                oAndTJoke.getTranslatedPunchline(),
                oAndTJoke.getOriginalLanguage(),
                oAndTJoke.getTargetLanguage()
        );
    }
}
