package ru.hse.rekoder.repositories.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.hse.rekoder.model.User;

import java.util.Optional;

//@Repository
public class MongoUserRepository /*implements UserRepository*/ {
    private static final String COLLECTION_NAME = "problem_owners";

    private final MongoOperations mongoOperations;

    public MongoUserRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }


    //@Override
    public Optional<User> findById(String name) {
        return Optional.ofNullable(mongoOperations.findById(new User.UserCompositeKey(name),
                                                            User.class,
                                                            COLLECTION_NAME));
    }

    //@Override
    public User save(User newUser) {
        if (newUser.getId() == null) {
            newUser.setId(new User.UserCompositeKey(newUser.getUsername()));
        }
        return mongoOperations.save(newUser, COLLECTION_NAME);
    }

    //@Override
    public boolean exists(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(id));
        return mongoOperations.exists(query, User.class, COLLECTION_NAME);
    }

    //@Override
    public long count() {
        return mongoOperations.count(new Query(), User.class, COLLECTION_NAME);
    }
}
