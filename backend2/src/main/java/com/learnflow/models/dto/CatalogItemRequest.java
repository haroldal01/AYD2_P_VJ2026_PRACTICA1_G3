package com.learnflow.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CatalogItemRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre no debe exceder 80 caracteres")
        String name,

        @Size(max = 255, message = "La descripcion no debe exceder 255 caracteres")
        String description,

        Boolean active
) {
}
