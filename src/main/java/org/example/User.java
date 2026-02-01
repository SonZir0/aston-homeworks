package org.example;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    String email;
    int age;

    @Column(name = "created_at")
    LocalDate createdAt;

    public User() {
    }

    public User(String name, String email, int age) {
        this(name, email, age, LocalDate.now());
    }

    public User(String name, String email, int age, LocalDate createdAt) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("\nId: %-10d\tName: %-12s\tEmail: %-16s\tAge: %-3d\tCreatedAt: %-10s",
                id, name, email, age, createdAt);
    }
}
