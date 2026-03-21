package org.example.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.user_service.dto.UserRequestDto;
import org.example.user_service.dto.UserResponseDto;
import org.example.user_service.exceptions.UserNotFoundException;
import org.example.user_service.mapper.UserHateoasModelAssembler;
import org.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/")
@CrossOrigin("*")
@Tag(name = "Пользователи", description = "Все методы для работы с пользователями системы")
public class UserController {
    private UserService userService;
    private UserHateoasModelAssembler modelAssembler;

    @Autowired
    UserController(UserService userService, UserHateoasModelAssembler modelAssembler) {
        this.userService = userService;
        this.modelAssembler = modelAssembler;
    }

    @PostMapping("users")
    @Operation(summary = "Добавить нового пользователя")
    public EntityModel<UserResponseDto> addNewUser(@Valid @RequestBody UserRequestDto newUserDto) {
        UserResponseDto responseDto = userService.addNewUser(newUserDto);
        return modelAssembler.toModel(responseDto);
    }

    @PutMapping("users/{id}")
    @Operation(summary = "Обновить данные пользователя с ID")
    public EntityModel<UserResponseDto> updateUserRecord(@PathVariable(value = "id") long userId,
                                            @Valid @RequestBody UserRequestDto userData) {
        UserResponseDto responseDto = userService.updateUserWithId(userId, userData)
                .orElseThrow( () -> new UserNotFoundException(userId));
        return modelAssembler.toModel(responseDto);
    }

    @GetMapping("users/{id}")
    @Operation(summary = "Получить информацию о пользователе с ID")
    public EntityModel<UserResponseDto> findUserById(@PathVariable(value= "id") long userId) {
        UserResponseDto responseDto = userService.findUserById(userId)
                .orElseThrow( () -> new UserNotFoundException(userId));
        return modelAssembler.toModel(responseDto);
    }

    @GetMapping("users")
    @Operation(summary = "Получить информацию о всех пользователях")
    public CollectionModel<EntityModel<UserResponseDto>> getListOfUsers() {
        List<EntityModel<UserResponseDto>> users = userService.getListOfUsers()
                .stream()
                .map(modelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getListOfUsers()).withSelfRel());
    }

    @DeleteMapping("users/{id}")
    @Operation(summary = "Удалить из системы пользователя с ID")
    public EntityModel<UserResponseDto> removeUserRecordById(@PathVariable(value = "id") long userId) {
        UserResponseDto responseDto = userService.getAndRemoveUserById(userId)
                .orElseThrow( () -> new UserNotFoundException(userId));
        return EntityModel.of(responseDto, linkTo(methodOn(UserController.class).getListOfUsers()).withRel("users"));
    }
}
