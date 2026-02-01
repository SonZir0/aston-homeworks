package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class UserDao implements Dao<User> {
    @Override
    public void save(User user) {
        BiConsumer<Session, User> saveOperation = Session::persist;
        dbSingularOperationLogic(user, saveOperation);
    }

    @Override
    public Optional<User> get(long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(User.class, id));
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return (List<User>) session.createQuery("FROM User u").list();
        }
    }

    @Override
    public void update(User user) {
        BiConsumer<Session, User> mergeOperation = Session::merge;
        dbSingularOperationLogic(user, mergeOperation);
    }

    @Override
    public void delete(User user) {
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
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}