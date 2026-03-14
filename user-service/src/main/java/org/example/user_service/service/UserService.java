package org.example.user_service.service;

import org.example.user_service.dto.UserRequestDto;
import org.example.user_service.dto.UserResponseDto;
import org.example.user_service.models.User;
import org.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailNotificationProducer msgProducer;

    @Autowired
    public UserService(UserRepository userRepository, EmailNotificationProducer msgProducer) {
        this.userRepository = userRepository;
        this.msgProducer = msgProducer;
    }

    public UserResponseDto addNewUser(UserRequestDto newUserData) {
        User temp = userRepository.save( new User(Objects.requireNonNull(newUserData)) );
        msgProducer.sendEmailNotification(temp.getEmail(),
                "Здравствуйте! Ваш аккаунт на сайте был успешно создан.");

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
                    msgProducer.sendEmailNotification( user.getEmail(),
                            "Здравстувуйте! Ваш аккаунт был удален");
                    return UserResponseDto.fromEntity(user);
                }));
    }

    public void removeUserById(long id) {
        userRepository.deleteById(id);
    }
}
