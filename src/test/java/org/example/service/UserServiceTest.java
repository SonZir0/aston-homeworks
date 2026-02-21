package org.example.service;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.models.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private EmailNotificationProducer mockMsgProducer;

    private static UserRequestDto[] testUserArr;

    @BeforeAll
    public static void setTestData() {
        testUserArr = new UserRequestDto[]{
                new UserRequestDto("aaa", "a@a", 12),
                new UserRequestDto("bbb", "b@b", 23),
                new UserRequestDto("ccc", "c@c", 34),
                new UserRequestDto("ddd", "d@d", 45)};
    }

    @Test
    void addNewUser_CallsSaveMethod() {
        when(mockUserRepository.save(any()))
                .thenReturn(new User(testUserArr[0]));

        userService.addNewUser(testUserArr[0]);
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    void addNewUser_SendsMailIfUserIsAdded() {
        when(mockUserRepository.save(any()))
                .thenReturn(new User(testUserArr[2]));

        userService.addNewUser(testUserArr[2]);
        verify(mockMsgProducer, times(1)).sendEmailNotification(any(), any());
    }

    @Test
    void addNewUser_NullArgThrowsException() {
        assertThrowsExactly(NullPointerException.class, () -> userService.addNewUser(null));
    }

    @Test
    void findUserById_GetsSpecifiedUserObj() {
        when(mockUserRepository.findById(eq(3L)))
                .thenReturn(Optional.of(
                        new User(testUserArr[2])));

        UserResponseDto tempDto = userService.findUserById(3L).get();
        assertAll("User by ID properties",
                () -> assertEquals(testUserArr[2].name(), tempDto.name()),
                () -> assertEquals(testUserArr[2].email(), tempDto.email()),
                () -> assertEquals(testUserArr[2].age(), tempDto.age()));
    }

    @Test
    void getListOfUsers() {
        when(mockUserRepository.findAll()).thenReturn(
                Arrays.stream(testUserArr)
                        .map(User::new)
                        .toList());
        assertEquals(4, userService.getListOfUsers().size());
    }

    @Test
    void updateUserRecord_CallsUpdateMethod() {
        when(mockUserRepository.findById(any()))
                .thenReturn(Optional.of(new User(testUserArr[0])));
        when(mockUserRepository.save(any()))
                .thenReturn(new User(testUserArr[0]));

        userService.updateUserWithId(1L, testUserArr[0]);
        userService.updateUserWithId(3L, testUserArr[2]);
        verify(mockUserRepository, times(2)).save(any(User.class));
    }

    @Test
    void updateUserWithId_NullArgThrowsException() {
        when(mockUserRepository.findById(eq(1L)))
                .thenReturn(Optional.of(new User(testUserArr[0])));
        assertThrowsExactly(NullPointerException.class, () -> userService.updateUserWithId(1L,null));
    }

    @Test
    void removeUserRecordById_CallsRemoveMethod() {
        userService.removeUserById(3);
        userService.removeUserById(4);
        verify(mockUserRepository, times(2)).deleteById(any());
    }

    @Test
    void removeUserRecordById_IsIdempotent() {
        assertDoesNotThrow(() -> {
            userService.removeUserById(33);
            userService.removeUserById(33);
            userService.removeUserById(400);
            userService.removeUserById(400);
        }, "Не бросает ошибку при попытки удаления не существующего элемента");
        verify(mockUserRepository, times(4)).deleteById(any());
    }

    @Test
    void getAndRemoveUserById_CallsDeleteOnlyIfUserIsFound() {
        when(mockUserRepository.findById(eq(10L)))
                .thenReturn(Optional.empty());
        when(mockUserRepository.findById(eq(3L)))
                .thenReturn(Optional.of(new User(testUserArr[3])));
        userService.getAndRemoveUserById(10);
        userService.getAndRemoveUserById(3);
        verify(mockUserRepository, times(1)).delete(any(User.class));
    }

    @Test
    void getAndRemoveUserById_SendsMailIfUserIsDeleted() {
        when(mockUserRepository.findById(any()))
                .thenReturn(Optional.of(new User(testUserArr[3])));
        when(mockUserRepository.findById(eq(10L)))
                .thenReturn(Optional.empty());

        userService.getAndRemoveUserById(1);
        userService.getAndRemoveUserById(3);
        userService.getAndRemoveUserById(10);
        verify(mockMsgProducer, times(2)).sendEmailNotification(any(), any());
    }
}