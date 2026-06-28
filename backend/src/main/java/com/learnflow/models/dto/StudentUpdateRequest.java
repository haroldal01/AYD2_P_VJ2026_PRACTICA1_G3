package com.learnflow.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StudentUpdateRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no debe exceder 150 caracteres")
        String fullName,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
        LocalDate dateOfBirth,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato valido")
        @Size(max = 100, message = "El correo no debe exceder 100 caracteres")
        String email,

        @Size(min = 6, max = 100, message = "La contrasena debe tener entre 6 y 100 caracteres")
        String password,

        @NotBlank(message = "El NIT es obligatorio")
        @Size(max = 20, message = "El NIT no debe exceder 20 caracteres")
        String nit,

        @NotBlank(message = "El numero de tarjeta es obligatorio")
        @Pattern(regexp = "\\d{13,19}", message = "La tarjeta debe contener entre 13 y 19 digitos")
        String cardNumber,

        @NotNull(message = "La fecha de vencimiento de la tarjeta es obligatoria")
        @Future(message = "La tarjeta debe tener una fecha de vencimiento futura")
        LocalDate cardExpiry,

        @NotBlank(message = "La fotografia es obligatoria")
        @Size(max = 500, message = "La fotografia no debe exceder 500 caracteres")
        String photoUrl
) {
}
