package org.example;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    void save(EntityManager session, T t);

    Optional<T> get(EntityManager session, long id);

    List<T> getAll(EntityManager session);

    void update(EntityManager session, T t);

    void delete(EntityManager session, T t);
}
