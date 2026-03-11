package org.example.notification_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendEmailRequestDto(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String subject,
        @NotBlank
        String body) {
}