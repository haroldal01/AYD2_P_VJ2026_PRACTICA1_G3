package com.learnflow.controllers;

import com.learnflow.models.dto.CatalogItemRequest;
import com.learnflow.models.dto.CatalogItemResponse;
import com.learnflow.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CatalogItemResponse> findAll() {
        return categoryService.findAll().stream()
                .map(CatalogItemResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public CatalogItemResponse findById(@PathVariable Long id) {
        return CatalogItemResponse.from(categoryService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CatalogItemResponse create(@Valid @RequestBody CatalogItemRequest request) {
        return CatalogItemResponse.from(categoryService.create(request));
    }

    @PutMapping("/{id}")
    public CatalogItemResponse update(@PathVariable Long id,
                                      @Valid @RequestBody CatalogItemRequest request) {
        return CatalogItemResponse.from(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
