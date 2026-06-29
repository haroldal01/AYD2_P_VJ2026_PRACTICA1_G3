package com.learnflow.controllers;

import com.learnflow.models.dto.CatalogItemRequest;
import com.learnflow.models.dto.CatalogItemResponse;
import com.learnflow.services.ContentTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content-types")
public class ContentTypeController {

    private final ContentTypeService contentTypeService;

    public ContentTypeController(ContentTypeService contentTypeService) {
        this.contentTypeService = contentTypeService;
    }

    @GetMapping
    public List<CatalogItemResponse> findAll() {
        return contentTypeService.findAll().stream()
                .map(CatalogItemResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public CatalogItemResponse findById(@PathVariable Long id) {
        return CatalogItemResponse.from(contentTypeService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CatalogItemResponse create(@Valid @RequestBody CatalogItemRequest request) {
        return CatalogItemResponse.from(contentTypeService.create(request));
    }

    @PutMapping("/{id}")
    public CatalogItemResponse update(@PathVariable Long id,
                                      @Valid @RequestBody CatalogItemRequest request) {
        return CatalogItemResponse.from(contentTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contentTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
