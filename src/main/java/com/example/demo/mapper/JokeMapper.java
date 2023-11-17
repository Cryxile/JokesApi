package com.example.demo.mapper;

import com.example.demo.dto.JokeDTO;
import com.example.demo.dto.RawJokeDTO;
import com.example.demo.model.Joke;
import com.example.demo.model.RawJoke;
import com.example.demo.model.db.JokeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JokeMapper {
    Joke mapJokeDtoToJoke(JokeDTO jokeDTO);

    Joke mapJokeEntityToJoke(JokeEntity jokeEntity);

    JokeDTO mapJokeToJokeDto(Joke joke);

    RawJoke mapRawJokeDtoToRawJoke(RawJokeDTO rawJokeDTO);

    JokeEntity mapJokeToJokeEntity(Joke joke);
}
