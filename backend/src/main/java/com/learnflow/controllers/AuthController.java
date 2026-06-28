package com.learnflow.controllers;

import com.learnflow.models.dto.AuthResponse;
import com.learnflow.models.dto.LoginRequest;
import com.learnflow.models.dto.StudentRegisterRequest;
import com.learnflow.models.entity.Student;
import com.learnflow.security.JwtUtil;
import com.learnflow.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final StudentService studentService;
    private final JwtUtil jwtUtil;

    public AuthController(StudentService studentService, JwtUtil jwtUtil) {
        this.studentService = studentService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/health")
    public String health() {
        return "LearnFlow API is running";
    }

    @PostMapping("/register")
    public AuthResponse registerStudent(@Valid @RequestBody StudentRegisterRequest request) {
        Student student = studentService.register(request);
        return buildAuthResponse(student);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        Student student = studentService.authenticate(request);
        return buildAuthResponse(student);
    }

    private AuthResponse buildAuthResponse(Student student) {
        String token = jwtUtil.generateToken(
                student.getUser().getEmail(),
                student.getUser().getId(),
                student.getUser().getRole().getName()
        );
        return AuthResponse.from(token, student);
    }
}
