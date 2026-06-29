package com.learnflow.models.dto;

import com.learnflow.models.entity.Course;

public record CourseResponse(
        Long id,
        String title,
        Integer productionYear,
        String instructor,
        String shortSummary,
        String description,
        String mediaUrl,
        CatalogItemResponse contentType,
        CatalogItemResponse category,
        CatalogItemResponse difficultyLevel,
        Boolean active
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getProductionYear(),
                course.getInstructor(),
                course.getShortSummary(),
                course.getDescription(),
                course.getMediaUrl(),
                CatalogItemResponse.from(course.getContentType()),
                CatalogItemResponse.from(course.getCategory()),
                CatalogItemResponse.from(course.getDifficultyLevel()),
                course.getActive()
        );
    }
}
