package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Integer>, UpdatableRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    List<User> findAllByUsername(Iterable<String> username);
    boolean existsByUsername(String username);
}
