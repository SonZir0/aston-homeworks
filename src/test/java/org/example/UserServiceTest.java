package org.example;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDao mockUserDao;
    private UserService userService;
    private User[] testUserArr;

    @BeforeEach
    void setUp() {
        userService = new UserService(mockUserDao);
        testUserArr = new User[]{
                new User("aaa", "a@a", 12),
                new User("bbb", "b@b", 23),
                new User("ccc", "c@c", 34),
                new User("ddd", "d@d", 45)};
    }

    @Test
    void addNewUser_CallsDaoSaveMethod() {
        userService.addNewUser(testUserArr[0]);
        userService.addNewUser(testUserArr[2]);
        userService.addNewUser(testUserArr[3]);
        verify(mockUserDao, times(3)).save(any(), any(User.class));
    }

    @Test
    void addNewUser_NullArgThrowsException() {
        assertThrowsExactly(NullPointerException.class, () -> userService.addNewUser(null));
    }

    @Test
    void findUserById_GetsSpecifiedUserObj() {
        when(mockUserDao.get(any(),eq(3L))).thenReturn(Optional.of(testUserArr[2]));
        User tempUser = userService.findUserById(3).get();
        assertAll("User by ID properties",
                () -> assertEquals("ccc", tempUser.getName()),
                () -> assertEquals("c@c", tempUser.getEmail()),
                () -> assertEquals(34, tempUser.getAge()));
    }

    @Test
    void getListOfUsers() {
        when(mockUserDao.getAll(any())).thenReturn(List.of(testUserArr));
        assertEquals(4, userService.getListOfUsers().size());
    }

    @Test
    void updateUserRecord_CallsDaoUpdateMethod() {
        userService.updateUserRecord(testUserArr[0]);
        userService.updateUserRecord(testUserArr[2]);
        verify(mockUserDao, times(2)).update(any(), any(User.class));
    }

    @Test
    void updateUserRecord_NullArgThrowsException() {
        assertThrowsExactly(NullPointerException.class, () -> userService.updateUserRecord(null));
    }

    @Test
    void removeUserRecordById_CallsDeleteOnlyIfUserIsntNull() {
        when(mockUserDao.get(any(),eq(10L))).thenReturn(Optional.empty());
        when(mockUserDao.get(any(),eq(3L))).thenReturn(Optional.of(testUserArr[3]));
        userService.removeUserRecordById(10);
        userService.removeUserRecordById(3);
        verify(mockUserDao, times(1)).delete(any(), any(User.class));
    }
}