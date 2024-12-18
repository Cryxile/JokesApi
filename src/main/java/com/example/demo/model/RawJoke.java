package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawJoke {
    private String type;
    private String setup;
    private String punchline;
    private Integer id;
}
