package org.example.user_service.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Не удалось найти пользователя с ID: " + id);
    }
}
