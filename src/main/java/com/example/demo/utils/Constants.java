package com.example.demo.utils;

public class Constants {

    public final static String RAPID_KEY = "X-RapidAPI-Key";
    public final static String RAPID_HOST = "X-RapidAPI-Host";
    public final static String ORIGINAL_LANGUAGE = "en";
    public final static String FILE_CREATION_PATH = "src/main/java/com/example/demo/db/";
    public final static String JOKES_FILE_PATH = "src/main/java/com/example/demo/db/jokes.json";
    public final static String META_FILE_PATH = "src/main/java/com/example/demo/db/jokes_meta.txt";
    public final static String FILE_ACCESS_MODE = "rw";
    public final static String FILE_EXTENSION_TYPE = ".tmp";
    public final static String MESSAGE_FORMAT_TEMPLATE = """
                        Joke № %d
                                        
                        Topic: %s
                                        
                        %s version:
                                        
                        Joke: %s
                        Punchline: %s
                                        
                        %s version:
                                        
                        Шутка: %s
                        Разрывная: %s
                        """;
}
