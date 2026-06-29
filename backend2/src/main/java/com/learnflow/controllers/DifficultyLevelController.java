package com.learnflow.controllers;

import com.learnflow.models.dto.CatalogItemRequest;
import com.learnflow.models.dto.CatalogItemResponse;
import com.learnflow.services.DifficultyLevelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/difficulty-levels")
public class DifficultyLevelController {

    private final DifficultyLevelService difficultyLevelService;

    public DifficultyLevelController(DifficultyLevelService difficultyLevelService) {
        this.difficultyLevelService = difficultyLevelService;
    }

    @GetMapping
    public List<CatalogItemResponse> findAll() {
        return difficultyLevelService.findAll().stream()
                .map(CatalogItemResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public CatalogItemResponse findById(@PathVariable Long id) {
        return CatalogItemResponse.from(difficultyLevelService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CatalogItemResponse create(@Valid @RequestBody CatalogItemRequest request) {
        return CatalogItemResponse.from(difficultyLevelService.create(request));
    }

    @PutMapping("/{id}")
    public CatalogItemResponse update(@PathVariable Long id,
                                      @Valid @RequestBody CatalogItemRequest request) {
        return CatalogItemResponse.from(difficultyLevelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        difficultyLevelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
