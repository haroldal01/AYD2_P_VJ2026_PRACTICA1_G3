package com.learnflow.models.dto;

import com.learnflow.models.entity.Category;
import com.learnflow.models.entity.ContentType;
import com.learnflow.models.entity.DifficultyLevel;

public record CatalogItemResponse(
        Long id,
        String name,
        String description,
        Boolean active
) {
    public static CatalogItemResponse from(ContentType contentType) {
        return new CatalogItemResponse(
                contentType.getId(),
                contentType.getName(),
                contentType.getDescription(),
                contentType.getActive()
        );
    }

    public static CatalogItemResponse from(Category category) {
        return new CatalogItemResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getActive()
        );
    }

    public static CatalogItemResponse from(DifficultyLevel difficultyLevel) {
        return new CatalogItemResponse(
                difficultyLevel.getId(),
                difficultyLevel.getName(),
                difficultyLevel.getDescription(),
                difficultyLevel.getActive()
        );
    }
}
