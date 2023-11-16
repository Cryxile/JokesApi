package com.example.demo.repository;

import com.example.demo.model.db.JokeEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JokeRepository extends JpaRepository<JokeEntity, Integer> {
}
