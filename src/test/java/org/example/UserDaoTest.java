package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoTest {
    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("testDB")
            .withUsername("user")
            .withPassword("password");

    private static SessionFactory sessionFactory = null;
    private static Session currentSession = null;
    private static UserDao userDao;
    private static final User user1 = new User("test111", "test@111", 111);
    private static final User user2 = new User("222test", "222@test", 222);

    @BeforeEach
    void setUp() {
        // Programmatically configure Hibernate's SessionFactory using the container's dynamic properties
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.connection.driver_class", postgres.getDriverClassName());
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
        currentSession = sessionFactory.openSession();

        userDao = new UserDao();
        insert2UsersIntoDB();
    }

    @AfterEach
    void shutDown() {
        if (currentSession != null) currentSession.close();
        if (sessionFactory != null) sessionFactory.close();
    }

    @Test
    void save_SavesGivenUserObject() {
        currentSession.beginTransaction();
        assertEquals(2, userDao.getAll(currentSession).size());
        userDao.save(currentSession, new User("Test1", "test@1", 12));
        currentSession.getTransaction().commit();
        assertEquals(3, userDao.getAll(currentSession).size());
    }

    @Test
    void get_ReturnsCorrectUserObj() {
        User temp = userDao.get(currentSession, 2).orElse(null);
        assertNotNull(temp);
        assertAll("User by ID",
                () -> assertEquals(2, temp.getId()),
                () -> assertEquals("222test", temp.getName()),
                () -> assertEquals("222@test", temp.getEmail()),
                () -> assertEquals(222, temp.getAge()));
    }

    @Test
    void get_OptionalOfNullIfIdIsntFound() {
        User temp = userDao.get(currentSession, 999).orElse(null);
        assertNull(temp);
    }

    @Test
    void getAll_ReturnsListOfAllUsers() {
        List<User> tempList = userDao.getAll(currentSession);
        User user1 = tempList.getFirst();
        User user2 = tempList.get(1);
        assertAll("List with users",
                () -> assertEquals(2, tempList.size()),
                () -> assertEquals(1, user1.getId()),
                () -> assertEquals("test@111", user1.getEmail()),
                () -> assertEquals(222, user2.getAge()),
                () -> assertEquals("222test", user2.getName()));
    }

    @Test
    void update_SavesChangedObjFields() {
        String newName = "ChangedName";
        String newEmail = "ChangedEmail";
        int newAge = 99999;

        currentSession.beginTransaction();
        User userToUpdate = userDao.get(currentSession, 1).orElse(null);
        assertNotNull(userToUpdate);

        userToUpdate.setName(newName);
        userToUpdate.setEmail(newEmail);
        userToUpdate.setAge(newAge);
        userDao.update(currentSession, userToUpdate);
        currentSession.getTransaction().commit();

        User resultingUser = userDao.get(currentSession, 1).orElse(null);
        assertNotNull(resultingUser);
        assertAll("Verifying update result",
                () -> assertEquals(newName, resultingUser.getName()),
                () -> assertEquals(newEmail, resultingUser.getEmail()),
                () -> assertEquals(newAge, resultingUser.getAge()));
    }

    @Test
    void delete_RemovesObjectsFromDb() {
        currentSession.beginTransaction();
        assertEquals(2, userDao.getAll(currentSession).size());

        User temp = userDao.get(currentSession, 1).orElse(null);
        assertNotNull(temp);
        userDao.delete(currentSession, temp);
        currentSession.getTransaction().commit();

        assertEquals(1, userDao.getAll(currentSession).size());
    }

    private void insert2UsersIntoDB() {
        currentSession.beginTransaction();
        currentSession.merge(user1);
        currentSession.merge(user2);
        currentSession.getTransaction().commit();
    }
}