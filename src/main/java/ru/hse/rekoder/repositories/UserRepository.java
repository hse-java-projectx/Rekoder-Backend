package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, User.UserCompositeKey> {
    Optional<User> findById(User.UserCompositeKey id);
    List<User> findAllById(Iterable<User.UserCompositeKey> id);
    boolean existsById(User.UserCompositeKey id);
}
