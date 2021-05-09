package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);
    User save(User newUser);
    boolean exists(String id);
    long count();
}
