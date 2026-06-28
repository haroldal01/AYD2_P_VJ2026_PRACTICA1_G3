package com.learnflow.models.dto;

import com.learnflow.models.entity.Student;

public record AuthResponse(
        String token,
        Long userId,
        Long studentId,
        String email,
        String role,
        String fullName
) {
    public static AuthResponse from(String token, Student student) {
        return new AuthResponse(
                token,
                student.getUser().getId(),
                student.getId(),
                student.getUser().getEmail(),
                student.getUser().getRole().getName(),
                student.getFullName()
        );
    }
}
