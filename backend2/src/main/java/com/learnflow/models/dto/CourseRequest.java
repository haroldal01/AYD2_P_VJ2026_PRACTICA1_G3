package com.learnflow.models.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CourseRequest(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 150, message = "El titulo no debe exceder 150 caracteres")
        String title,

        @NotNull(message = "El anio de produccion es obligatorio")
        @Min(value = 1990, message = "El anio de produccion debe ser mayor o igual a 1990")
        @Max(value = 2100, message = "El anio de produccion no debe exceder 2100")
        Integer productionYear,

        @NotBlank(message = "El instructor es obligatorio")
        @Size(max = 120, message = "El instructor no debe exceder 120 caracteres")
        String instructor,

        @NotBlank(message = "El resumen breve es obligatorio")
        @Size(max = 255, message = "El resumen breve no debe exceder 255 caracteres")
        String shortSummary,

        @NotBlank(message = "La descripcion es obligatoria")
        String description,

        @NotBlank(message = "La URL del contenido es obligatoria")
        @Size(max = 500, message = "La URL del contenido no debe exceder 500 caracteres")
        String mediaUrl,

        @NotNull(message = "El tipo de contenido es obligatorio")
        Long contentTypeId,

        @NotNull(message = "La categoria es obligatoria")
        Long categoryId,

        @NotNull(message = "El nivel de dificultad es obligatorio")
        Long difficultyLevelId,

        Boolean active
) {
}
