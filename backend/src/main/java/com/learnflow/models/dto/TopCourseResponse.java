package com.learnflow.models.dto;

import com.learnflow.models.entity.Course;

public record TopCourseResponse(
        Long courseId,
        String title,
        String category,
        String contentType,
        String instructor,
        String shortSummary,
        Long viewCount
) {
    public static TopCourseResponse from(Course course, Long viewCount) {
        return new TopCourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCategory().getName(),
                course.getContentType().getName(),
                course.getInstructor(),
                course.getShortSummary(),
                viewCount
        );
    }
}
