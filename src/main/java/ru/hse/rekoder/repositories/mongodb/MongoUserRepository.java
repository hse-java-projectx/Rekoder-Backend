package ru.hse.rekoder.repositories.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.repositories.UserRepository;

import java.util.Optional;

@Repository
public class MongoUserRepository implements UserRepository {
    private final MongoOperations mongoOperations;

    public MongoUserRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }


    @Override
    public Optional<User> findById(String name) {
        return Optional.ofNullable(mongoOperations.findById(new User.UserCompositeKey(name), User.class, "problemOwner"));
    }

    @Override
    public User save(User newUser) {
        if (newUser.getId() == null) {
            newUser.setId(new User.UserCompositeKey(newUser.getName()));
        }
        return mongoOperations.save(newUser, "problemOwner");
    }

    @Override
    public boolean exists(String id) {
        //TODO!!!!!!
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        return 0;
    }
}
