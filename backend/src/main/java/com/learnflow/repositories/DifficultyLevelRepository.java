package com.learnflow.repositories;

import com.learnflow.models.entity.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DifficultyLevelRepository extends JpaRepository<DifficultyLevel, Long> {
    Optional<DifficultyLevel> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
