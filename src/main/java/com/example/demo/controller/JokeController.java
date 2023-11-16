package com.example.demo.controller;

import com.example.demo.dto.JokeDTO;
import com.example.demo.dto.RawJokeDTO;
import com.example.demo.model.Joke;
import com.example.demo.model.RawJoke;
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
    public JokeDTO getRandomJoke() {
        return new JokeDTO(jokeService.getRandomJoke());
    }

    @GetMapping("/jokes")
    public List<Joke> getJokeList() {
        return jokeService.getJokeList();
    }

    @PutMapping("/joke/add")
    public ResponseEntity<String> addJoke(@RequestBody RawJokeDTO rawJokeDTO) {
        jokeService.addJoke(new RawJoke(rawJokeDTO));
        return new ResponseEntity<>("Joke successfully added!", HttpStatus.OK);
    }

    @DeleteMapping("/joke/{id}")
    public ResponseEntity<String> deleteJoke(@PathVariable("id") int id) {
        jokeService.deleteJoke(id);
        return new ResponseEntity<>("Joke successfully deleted!", HttpStatus.OK);
    }

    @PostMapping("/joke/edit")
    public ResponseEntity<String> editJoke(@RequestBody JokeDTO jokeDTO) {
        jokeService.editJoke(new Joke(jokeDTO));
        return new ResponseEntity<>("Joke successfully edited!", HttpStatus.OK);
    }

    @GetMapping("/joke/{id}")
    public JokeDTO getJoke(@PathVariable("id") Integer id) {
        return new JokeDTO(jokeService.getJoke(id));
    }

    @GetMapping("/send-joke/{id}")
    public ResponseEntity<String> sendJoke(@PathVariable("id") Integer id) {
        jokeService.send(id);
        return new ResponseEntity<>("Mail have sent successfully!", HttpStatus.OK);
    }

    @GetMapping("/send-joke/random")
    public ResponseEntity<String> sendJoke() {
        jokeService.send(jokeService.getRandomJoke());
        return new ResponseEntity<>("Mail have sent successfully!", HttpStatus.OK);
    }
}
