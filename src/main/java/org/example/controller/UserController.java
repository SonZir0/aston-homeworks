package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class UserController {
    private UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    public UserResponseDto addNewUser(@Valid @RequestBody UserRequestDto newUserDto) {
        return userService.addNewUser(newUserDto);
    }

    @PutMapping("users/{id}")
    public UserResponseDto updateUserRecord(@PathVariable(value = "id") long userId,
                                            @Valid @RequestBody UserRequestDto userData) {
        return userService.updateUserWithId(userId, userData).orElse(null);
    }

    @GetMapping("users/{id}")
    public UserResponseDto findUserById(@PathVariable(value= "id") long userId) {
        return userService.findUserById(userId).orElse(null);
    }

    @GetMapping("users")
    public List<UserResponseDto> getListOfUsers() {
        return userService.getListOfUsers();
    }

    @DeleteMapping("users/{id}")
    public UserResponseDto removeUserRecordById(@PathVariable(value = "id") long userId) {
        return userService.getAndRemoveUserById(userId).orElse(null);
    }
}
