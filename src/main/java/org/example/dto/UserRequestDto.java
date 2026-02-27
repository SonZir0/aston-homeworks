package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

public record UserRequestDto(
        @NotBlank
        String name,
        @NotBlank
        @Email
        String email,
        @Min(value = 0)
        int age) {}