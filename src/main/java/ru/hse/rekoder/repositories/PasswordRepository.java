package ru.hse.rekoder.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.Password;

public interface PasswordRepository extends MongoRepository<Password, ObjectId> {
}
