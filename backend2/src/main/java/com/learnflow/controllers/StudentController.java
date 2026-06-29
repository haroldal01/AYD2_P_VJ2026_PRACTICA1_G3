package com.learnflow.controllers;

import com.learnflow.models.dto.StudentResponse;
import com.learnflow.models.dto.StudentUpdateRequest;
import com.learnflow.models.entity.Student;
import com.learnflow.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/me")
    public StudentResponse getProfile(Authentication authentication) {
        return StudentResponse.from(studentService.findByUserEmail(authentication.getName()));
    }

    @PutMapping("/me")
    public StudentResponse updateProfile(Authentication authentication,
                                         @Valid @RequestBody StudentUpdateRequest request) {
        Student student = studentService.updateCurrentStudent(authentication.getName(), request);
        return StudentResponse.from(student);
    }
}
