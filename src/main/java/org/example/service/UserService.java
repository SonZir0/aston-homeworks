package org.example.service;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.models.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto addNewUser(UserRequestDto newUserData) {
        User temp = userRepository.save( new User(Objects.requireNonNull(newUserData)) );

        return UserResponseDto.fromEntity(temp);
    }

    public Optional<UserResponseDto> updateUserWithId(long id, UserRequestDto newUserData) {
        return userRepository.findById(id).
                map((user) -> {
                    user.updateWithValuesFrom(Objects.requireNonNull(newUserData));
                    return UserResponseDto.fromEntity(userRepository.save(user));
        });
    }

    public Optional<UserResponseDto> findUserById(long id) {
        return userRepository.findById(id)
                .map(UserResponseDto::fromEntity);
    }

    public List<UserResponseDto> getListOfUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::fromEntity).
                toList();
    }

    public Optional<UserResponseDto> getAndRemoveUserById(long id) {
        return userRepository.findById(id)
                .map((user -> {
                    userRepository.delete(user);
                    return UserResponseDto.fromEntity(user);
                }));
    }

    public void removeUserById(long id) {
        userRepository.deleteById(id);
    }
}
