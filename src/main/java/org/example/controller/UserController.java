package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "Пользователи", description = "Все методы для работы с пользователями системы")
public class UserController {
    private UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    @Operation(summary = "Добавить нового пользователя")
    public UserResponseDto addNewUser(@Valid @RequestBody UserRequestDto newUserDto) {
        return userService.addNewUser(newUserDto);
    }

    @PutMapping("users/{id}")
    @Operation(summary = "Обновить данные пользователя с ID")
    public UserResponseDto updateUserRecord(@PathVariable(value = "id") long userId,
                                            @Valid @RequestBody UserRequestDto userData) {
        return userService.updateUserWithId(userId, userData).orElse(null);
    }

    @GetMapping("users/{id}")
    @Operation(summary = "Получить информацию о пользователе с ID")
    public UserResponseDto findUserById(@PathVariable(value= "id") long userId) {
        return userService.findUserById(userId).orElse(null);
    }

    @GetMapping("users")
    @Operation(summary = "Получить информацию о всех пользователях")
    public List<UserResponseDto> getListOfUsers() {
        return userService.getListOfUsers();
    }

    @DeleteMapping("users/{id}")
    @Operation(summary = "Удалить из системы пользователя с ID")
    public UserResponseDto removeUserRecordById(@PathVariable(value = "id") long userId) {
        return userService.getAndRemoveUserById(userId).orElse(null);
    }
}
