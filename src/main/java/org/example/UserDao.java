package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class UserDao implements Dao<User> {
    private final Logger logger
            = LoggerFactory.getLogger(this.getClass());

    @Override
    public void save(User user) {
        logger.info("Saving user with parameters: {}", user);
        BiConsumer<Session, User> saveOperation = Session::persist;
        dbSingularOperationLogic(user, saveOperation);
    }

    @Override
    public Optional<User> get(long id) {
        logger.info("Trying to find user with ID: {}", id);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(User.class, id));
        }
    }

    @Override
    public List<User> getAll() {
        logger.info("Get all users from the table.");
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return (List<User>) session.createQuery("FROM User u").list();
        }
    }

    @Override
    public void update(User user) {
        logger.info("Updating the user with parameters: {}", user);
        BiConsumer<Session, User> mergeOperation = Session::merge;
        dbSingularOperationLogic(user, mergeOperation);
    }

    @Override
    public void delete(User user) {
        logger.info("Deleting user: {}", user);
        BiConsumer<Session, User> removeOperation = Session::remove;
        dbSingularOperationLogic(user, removeOperation);
    }

    public void dbSingularOperationLogic(User user, BiConsumer<Session, User> dbOperation) {
        if (user == null) return;
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            dbOperation.accept(session, user);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            logger.error("Exception operating on entity {}\nError class: {}\nError message: {}"
                    , user, e.getClass(), e.getMessage());
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}