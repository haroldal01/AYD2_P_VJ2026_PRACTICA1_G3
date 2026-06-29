package com.learnflow.models.dto;

import com.learnflow.models.entity.Course;

public record RecommendationResponse(
        Long courseId,
        String title,
        String category,
        String contentType,
        String difficultyLevel,
        String instructor,
        String shortSummary,
        String reason
) {
    public static RecommendationResponse from(Course course, String reason) {
        return new RecommendationResponse(
                course.getId(),
                course.getTitle(),
                course.getCategory().getName(),
                course.getContentType().getName(),
                course.getDifficultyLevel().getName(),
                course.getInstructor(),
                course.getShortSummary(),
                reason
        );
    }
}
