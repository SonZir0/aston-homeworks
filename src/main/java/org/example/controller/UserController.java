package org.example.controller;

import org.example.dto.UserDto;
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
    public UserDto addNewUser(@RequestBody UserDto newUserDto) {
        return userService.addNewUser(newUserDto);
    }

    @PutMapping("users/{id}")
    public UserDto updateUserRecord(@PathVariable(value = "id") long userId,
                                            @RequestBody UserDto userData) {
        return userService.updateUserWithId(userId, userData).orElse(null);
    }

    @GetMapping("users/{id}")
    public UserDto findUserById(@PathVariable(value= "id") long userId) {
        return userService.findUserById(userId).orElse(null);
    }

    @GetMapping("users")
    public List<UserDto> getListOfUsers() {
        return userService.getListOfUsers();
    }

    @DeleteMapping("users/{id}")
    public void removeUserRecordById(@PathVariable(value = "id") long userId) {
        userService.removeUserById(userId);
    }
}
