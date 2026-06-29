package com.learnflow.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        List<String> errors
) {
    public static ErrorResponse of(int status, String message, List<String> errors) {
        return new ErrorResponse(LocalDateTime.now(), status, message, errors);
    }
}
