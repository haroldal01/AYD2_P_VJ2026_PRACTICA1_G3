package com.learnflow.services;

import com.learnflow.models.dto.LoginRequest;
import com.learnflow.models.dto.StudentRegisterRequest;
import com.learnflow.models.dto.StudentUpdateRequest;
import com.learnflow.models.entity.Role;
import com.learnflow.models.entity.Student;
import com.learnflow.models.entity.User;
import com.learnflow.repositories.RoleRepository;
import com.learnflow.repositories.StudentRepository;
import com.learnflow.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class StudentService {

    private static final String STUDENT_ROLE = "ESTUDIANTE";

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Student register(StudentRegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }

        Role role = roleRepository.findByName(STUDENT_ROLE)
                .orElseThrow(() -> new IllegalStateException("No existe el rol ESTUDIANTE"));

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setUser(savedUser);
        applyStudentData(student, request.fullName(), request.dateOfBirth(), request.nit(),
                request.cardNumber(), request.cardExpiry(), request.photoUrl());

        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public Student authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(normalizeEmail(request.email()))
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!Boolean.TRUE.equals(user.getEnabled()) || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene perfil de estudiante"));
    }

    @Transactional(readOnly = true)
    public Student findByUserEmail(String email) {
        User user = userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Perfil de estudiante no encontrado"));
    }

    @Transactional
    public Student updateCurrentStudent(String currentEmail, StudentUpdateRequest request) {
        Student student = findByUserEmail(currentEmail);
        User user = student.getUser();
        String normalizedEmail = normalizeEmail(request.email());

        userRepository.findByEmail(normalizedEmail)
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("El correo ya esta registrado");
                });

        user.setEmail(normalizedEmail);
        if (StringUtils.hasText(request.password())) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        applyStudentData(student, request.fullName(), request.dateOfBirth(), request.nit(),
                request.cardNumber(), request.cardExpiry(), request.photoUrl());

        return studentRepository.save(student);
    }

    private void applyStudentData(Student student,
                                  String fullName,
                                  java.time.LocalDate dateOfBirth,
                                  String nit,
                                  String cardNumber,
                                  java.time.LocalDate cardExpiry,
                                  String photoUrl) {
        student.setFullName(fullName.trim());
        student.setDateOfBirth(dateOfBirth);
        student.setNit(nit.trim());
        student.setCardNumber(cardNumber.trim());
        student.setCardExpiry(cardExpiry);
        student.setPhotoUrl(photoUrl.trim());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
