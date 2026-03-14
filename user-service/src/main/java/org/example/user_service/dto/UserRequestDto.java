package org.example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

@Schema(description = "Информация о пользователе для запроса в API")
public record UserRequestDto(
        @NotBlank
        String name,
        @NotBlank
        @Email
        String email,
        @Min(value = 0)
        int age) {}