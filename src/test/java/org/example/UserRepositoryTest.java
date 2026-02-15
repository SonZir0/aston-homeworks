package org.example;

import org.example.models.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@Transactional
@DataJpaTest
class UserRepositoryTest {
    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("testDB")
            .withUsername("user")
            .withPassword("12345");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        userRepository.save(new User("test111", "test@111", 111));
        userRepository.save(new User("222test", "222@test", 222));
    }

    @Test
    void save_SavesGivenUserObject() {
        List<User> tempList = userRepository.findAll();
        System.out.println("tempList.size = " + tempList.size());

        assertEquals(2, userRepository.findAll().size());
        userRepository.save(new User("Test1", "test@1", 12));
        assertEquals(3, userRepository.findAll().size());
    }

    @Test
    void findByName_ReturnsCorrectUserObj() {
        User temp = userRepository.findByName("222test").orElse(null);
        assertNotNull(temp);
        assertAll("User by name",
                () -> assertEquals("222test", temp.getName()),
                () -> assertEquals("222@test", temp.getEmail()),
                () -> assertEquals(222, temp.getAge()));
    }

    @Test
    void findById_OptionalOfNullIfIdIsntFound() {
        User temp = userRepository.findById(999L).orElse(null);
        assertNull(temp);
    }

    @Test
    void findAll_ReturnsListOfAllUsers() {
        List<User> tempList = userRepository.findAll();
        User user1 = tempList.get(0);
        User user2 = tempList.get(1);
        assertAll("List with users",
                () -> assertEquals(2, tempList.size()),
                () -> assertEquals("test@111", user1.getEmail()),
                () -> assertEquals(222, user2.getAge()),
                () -> assertEquals("222test", user2.getName()));
    }

    @Test
    void update_SavesChangedObjFields() {
        String newName = "ChangedName";
        String newEmail = "ChangedEmail";
        int newAge = 99999;

        User userToUpdate = userRepository.findByName("test111").orElse(null);
        assertNotNull(userToUpdate);
        userToUpdate.setName(newName);
        userToUpdate.setEmail(newEmail);
        userToUpdate.setAge(newAge);
        long idOfUserToUpdate = userToUpdate.getId();
        userRepository.save(userToUpdate);

        User resultingUser = userRepository.findById(idOfUserToUpdate).orElse(null);
        assertNotNull(resultingUser);
        assertAll("Verifying update result",
                () -> assertEquals(newName, resultingUser.getName()),
                () -> assertEquals(newEmail, resultingUser.getEmail()),
                () -> assertEquals(newAge, resultingUser.getAge()));
    }

    @Test
    void delete_RemovesObjectsFromDb() {
        List<User> userList = userRepository.findAll();
        assertEquals(2, userList.size());
        userRepository.delete(userList.get(0));

        assertEquals(1, userRepository.findAll().size());
    }
}