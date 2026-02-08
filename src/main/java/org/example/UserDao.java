package org.example;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User> {
    private final Logger logger
            = LoggerFactory.getLogger(this.getClass());

    @Override
    public void save(EntityManager session, User user) {
        logger.info("Saving user with parameters: {}", user);
        session.persist(user);
    }

    @Override
    public Optional<User> get(EntityManager session, long id) {
        logger.info("Trying to find user with ID: {}", id);
        return Optional.ofNullable(session.find(User.class, id));
    }

    @Override
    public List<User> getAll(EntityManager session) {
        logger.info("Get all users from the table.");
        return (List<User>) session.createQuery("FROM User u").getResultList();
    }

    @Override
    public void update(EntityManager session, User user) {
        logger.info("Updating the user with parameters: {}", user);
        session.merge(user);
    }

    @Override
    public void delete(EntityManager session, User user) {
        logger.info("Deleting user: {}", user);
        session.remove(user);
    }
}