package com.learnflow.services;

import com.learnflow.models.dto.CatalogItemRequest;
import com.learnflow.models.entity.DifficultyLevel;
import com.learnflow.repositories.DifficultyLevelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DifficultyLevelService {

    private final DifficultyLevelRepository difficultyLevelRepository;

    public DifficultyLevelService(DifficultyLevelRepository difficultyLevelRepository) {
        this.difficultyLevelRepository = difficultyLevelRepository;
    }

    @Transactional(readOnly = true)
    public List<DifficultyLevel> findAll() {
        return difficultyLevelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DifficultyLevel findById(Long id) {
        return difficultyLevelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nivel de dificultad no encontrado"));
    }

    @Transactional
    public DifficultyLevel create(CatalogItemRequest request) {
        String name = normalize(request.name());
        if (difficultyLevelRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("El nivel de dificultad ya existe");
        }

        DifficultyLevel difficultyLevel = new DifficultyLevel();
        applyData(difficultyLevel, request);
        return difficultyLevelRepository.save(difficultyLevel);
    }

    @Transactional
    public DifficultyLevel update(Long id, CatalogItemRequest request) {
        DifficultyLevel difficultyLevel = findById(id);
        String name = normalize(request.name());
        difficultyLevelRepository.findByNameIgnoreCase(name)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("El nivel de dificultad ya existe");
                });

        applyData(difficultyLevel, request);
        return difficultyLevelRepository.save(difficultyLevel);
    }

    @Transactional
    public void delete(Long id) {
        DifficultyLevel difficultyLevel = findById(id);
        difficultyLevelRepository.delete(difficultyLevel);
    }

    private void applyData(DifficultyLevel difficultyLevel, CatalogItemRequest request) {
        difficultyLevel.setName(normalize(request.name()));
        difficultyLevel.setDescription(cleanOptional(request.description()));
        difficultyLevel.setActive(request.active() == null || request.active());
    }

    private String normalize(String value) {
        return value.trim();
    }

    private String cleanOptional(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
