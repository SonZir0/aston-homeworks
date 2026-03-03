package org.example.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Не удалось найти пользователя с ID: " + id);
    }
}
