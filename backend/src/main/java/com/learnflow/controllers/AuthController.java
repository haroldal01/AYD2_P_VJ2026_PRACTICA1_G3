package com.learnflow.controllers;

import com.learnflow.models.dto.AuthResponse;
import com.learnflow.models.dto.LoginRequest;
import com.learnflow.models.dto.StudentRegisterRequest;
import com.learnflow.models.entity.Student;
import com.learnflow.models.entity.User;
import com.learnflow.security.JwtUtil;
import com.learnflow.services.StudentService;
import com.learnflow.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final StudentService studentService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(StudentService studentService,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.studentService = studentService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/health")
    public String health() {
        return "LearnFlow API is running";
    }

    @PostMapping("/register")
    public AuthResponse registerStudent(@Valid @RequestBody StudentRegisterRequest request) {
        Student student = studentService.register(request);
        return buildStudentAuthResponse(student);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        Student student = studentService.authenticate(request);
        return buildStudentAuthResponse(student);
    }

    @PostMapping("/admin/login")
    public AuthResponse adminLogin(@Valid @RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cuenta deshabilitada");
        }
        if (!"ADMIN_CONTENIDO".equals(user.getRole().getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado para este perfil");
        }
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().getName());
        return AuthResponse.fromAdmin(token, user);
    }

    private AuthResponse buildStudentAuthResponse(Student student) {
        String token = jwtUtil.generateToken(
                student.getUser().getEmail(),
                student.getUser().getId(),
                student.getUser().getRole().getName()
        );
        return AuthResponse.from(token, student);
    }
}
