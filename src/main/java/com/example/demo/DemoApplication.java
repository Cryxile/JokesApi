package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.example.demo.utils.Constants.META_FILE_PATH;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
//        try (RandomAccessFile toMetaFile = new RandomAccessFile(META_FILE_PATH, "rw")) {
//            toMetaFile.seek(44);
//            System.out.println(toMetaFile.readLine());
//            System.out.println(toMetaFile.length());
//        } catch (IOException e){
//
//        }
        SpringApplication.run(DemoApplication.class, args);
    }
}
