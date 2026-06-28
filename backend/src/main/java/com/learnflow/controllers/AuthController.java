package com.learnflow.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/health")
    public String health() {
        return "LearnFlow API is running";
    }
}
