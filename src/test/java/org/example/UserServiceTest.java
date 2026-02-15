package org.example;

import org.example.dto.UserDto;
import org.example.models.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository mockUserRepository;

    private static User[] testUserArr;

    @BeforeAll
    public static void setTestData() {
        testUserArr = new User[]{
                new User("aaa", "a@a", 12),
                new User("bbb", "b@b", 23),
                new User("ccc", "c@c", 34),
                new User("ddd", "d@d", 45)};
        // создание для return'ов Dto требует наличия ID
        ReflectionTestUtils.setField(testUserArr[0], "id", 1L);
        ReflectionTestUtils.setField(testUserArr[1], "id", 2L);
        ReflectionTestUtils.setField(testUserArr[2], "id", 3L);
        ReflectionTestUtils.setField(testUserArr[3], "id", 4L);
    }

    @BeforeEach
    void setUp() {
        //userService = new UserService(mockUserRepository);
    }

    @Test
    void addNewUser_CallsSaveMethod() {
        when(mockUserRepository.save(any())).thenReturn(testUserArr[0]);
        userService.addNewUser(testUserArr[0]);
        userService.addNewUser(testUserArr[2]);
        userService.addNewUser(testUserArr[3]);
        verify(mockUserRepository, times(3)).save(any(User.class));
    }

    @Test
    void addNewUser_NullArgThrowsException() {
        assertThrowsExactly(NullPointerException.class, () -> userService.addNewUser(null));
    }

    @Test
    void findUserById_GetsSpecifiedUserObj() {
        when(mockUserRepository.findById(eq(3L))).thenReturn(Optional.of(testUserArr[2]));
        UserDto tempDto = userService.findUserById(3L).get();
        assertAll("User by ID properties",
                () -> assertEquals(testUserArr[2].getName(), tempDto.name()),
                () -> assertEquals(testUserArr[2].getEmail(), tempDto.email()),
                () -> assertEquals(testUserArr[2].getAge(), tempDto.age()));
    }

    @Test
    void getListOfUsers() {
        when(mockUserRepository.findAll()).thenReturn(List.of(testUserArr));
        assertEquals(4, userService.getListOfUsers().size());
    }

    @Test
    void updateUserRecord_CallsUpdateMethod() {
        when(mockUserRepository.findById(any())).thenReturn(Optional.of(testUserArr[0]));
        when(mockUserRepository.save(any())).thenReturn(testUserArr[0]);
        userService.updateUserWithId(1L, testUserArr[0]);
        userService.updateUserWithId(3L, testUserArr[2]);
        verify(mockUserRepository, times(2)).save(any(User.class));
    }

    @Test
    void updateUserWithId_NullArgThrowsException() {
        when(mockUserRepository.findById(eq(1L))).thenReturn(Optional.of(testUserArr[0]));
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
}