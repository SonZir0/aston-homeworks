package org.example.service;

import org.example.dto.UserDto;
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

    public UserDto addNewUser(User user) {
        return UserDto.fromEntity(
                userRepository.save(Objects.requireNonNull(user)));
    }

    public Optional<UserDto> updateUserWithId(long id, User newUserData) {
        return userRepository.findById(id).
                map((user) -> {
                    user.updateWithValuesFrom(Objects.requireNonNull(newUserData));
                    return UserDto.fromEntity(userRepository.save(user));
        });
    }

    public Optional<UserDto> findUserById(long id) {
        return userRepository.findById(id).
                map(UserDto::fromEntity);
    }

    public List<UserDto> getListOfUsers() {
        return userRepository.findAll().stream().
                map(UserDto::fromEntity).
                toList();
    }

    public void removeUserById(long id) {
        userRepository.deleteById(id);
    }
}
