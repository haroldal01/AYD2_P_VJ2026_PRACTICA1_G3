package com.learnflow.models.dto;

import com.learnflow.models.entity.Student;

import java.time.LocalDate;

public record StudentResponse(
        Long studentId,
        Long userId,
        String fullName,
        LocalDate dateOfBirth,
        String email,
        String nit,
        String cardNumber,
        LocalDate cardExpiry,
        String photoUrl
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getUser().getId(),
                student.getFullName(),
                student.getDateOfBirth(),
                student.getUser().getEmail(),
                student.getNit(),
                student.getCardNumber(),
                student.getCardExpiry(),
                student.getPhotoUrl()
        );
    }
}
