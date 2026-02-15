package org.example.controller;

import org.example.dto.UserDto;
import org.example.models.User;
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
    public UserDto addNewUser(@RequestBody User user) {
        return userService.addNewUser(user);
    }

    @PutMapping("users/{id}")
    public UserDto updateUserRecord(@PathVariable(value = "id") long userId,
                                    @RequestBody User user) {
        return userService.updateUserWithId(userId, user).orElse(null);
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
