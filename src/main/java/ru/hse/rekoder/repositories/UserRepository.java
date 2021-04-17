package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Integer id);
    User save(User newUser);
    long count();
}
