package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawJokeDTO {
    private String type;
    private String setup;
    private String punchline;
    private Integer id;
}
