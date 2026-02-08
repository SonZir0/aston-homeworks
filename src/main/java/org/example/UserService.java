package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addNewUser(User user) {
        BiConsumer<Session, User> saveUserLogic = userDao::save;
        createTransactionAndRunLogic(saveUserLogic, Objects.requireNonNull(user));
    }

    public Optional<User> findUserById(long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return userDao.get(session, id);
        }
    }

    public List<User> getListOfUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return userDao.getAll(session);
        }
    }

    public void updateUserRecord(User user) {
        BiConsumer<Session, User> updateUserLogic = userDao::update;
        createTransactionAndRunLogic(updateUserLogic, Objects.requireNonNull(user));

    }

    public void removeUserRecordById(long id) {
        BiConsumer<Session, Long> removeByID = (session, idToPass) -> {
            userDao.get(session, idToPass).
                    ifPresent((foundUser) -> userDao.delete(session, foundUser));
        };
        createTransactionAndRunLogic(removeByID, id);
    }

    private <T>void createTransactionAndRunLogic(BiConsumer<Session, T> dbTransactionLogic,
                                                 T relevantArg) {
        Transaction tx = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            dbTransactionLogic.accept(session, relevantArg);

            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
