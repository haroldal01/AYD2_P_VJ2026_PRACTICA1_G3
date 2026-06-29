package com.learnflow.services;

import com.learnflow.models.dto.CatalogItemRequest;
import com.learnflow.models.entity.Category;
import com.learnflow.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
    }

    @Transactional
    public Category create(CatalogItemRequest request) {
        String name = normalize(request.name());
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("La categoria ya existe");
        }
        Category category = new Category();
        applyData(category, request);
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, CatalogItemRequest request) {
        Category category = findById(id);
        String name = normalize(request.name());
        categoryRepository.findByNameIgnoreCase(name)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new IllegalArgumentException("La categoria ya existe"); });
        applyData(category, request);
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.delete(findById(id));
    }

    private void applyData(Category category, CatalogItemRequest request) {
        category.setName(normalize(request.name()));
        category.setDescription(cleanOptional(request.description()));
        category.setActive(request.active() == null || request.active());
    }

    private String normalize(String value) { return value.trim(); }

    private String cleanOptional(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
