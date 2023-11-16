package com.example.demo.model;


import com.example.demo.dto.RawJokeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawJoke {

    public RawJoke(RawJokeDTO joke) {
        type = joke.getType();
        setup = joke.getSetup();
        punchline = joke.getPunchline();
        id = joke.getId();
    }
    private String type;
    private String setup;
    private String punchline;
    private Integer id;
}
