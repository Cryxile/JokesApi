package com.example.demo.controller;

import com.example.demo.dto.AddJokeDTO;
import com.example.demo.model.Joke;
import com.example.demo.service.JokeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class JokeController {

    private final JokeService jokeService;

    @GetMapping("/joke/random-joke")
    public List<Joke> getRandomJoke() {
        return jokeService.getRandomJoke();
    }

    @GetMapping("/joke")
    public List<Joke> getJokeList() {
        return jokeService.getJokeList();
    }

    @PutMapping("/joke/add")
    public void addJoke(@RequestBody AddJokeDTO addJokeDTO) {
        jokeService.addJoke(addJokeDTO.getType(), addJokeDTO.getSetup(), addJokeDTO.getPunchline(), addJokeDTO.getId());
    }

    @DeleteMapping("/joke/{id}")
    public void deleteJoke(@PathVariable("id") Integer id) {
        jokeService.deleteJoke(id);
    }

    @PostMapping("/joke")
    public void editJoke(@RequestBody AddJokeDTO addJokeDTO) {
        jokeService.editJoke(addJokeDTO.getType(), addJokeDTO.getSetup(), addJokeDTO.getPunchline(), addJokeDTO.getId());
    }

    @GetMapping("/joke/{id}")
    public Joke getJoke(@PathVariable("id") Integer id) {
        return jokeService.getJoke(id);
    }
}
