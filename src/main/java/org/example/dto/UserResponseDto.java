package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.models.User;
import java.time.LocalDate;

@Schema(description = "Ответ с информацией о пользователе")
public record UserResponseDto(
        long id,
        String name,
        String email,
        int age,
        LocalDate createdAt) {

    public static UserResponseDto fromEntity(User user) {
        if (user == null) throw new IllegalArgumentException();
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt());
    }
}
