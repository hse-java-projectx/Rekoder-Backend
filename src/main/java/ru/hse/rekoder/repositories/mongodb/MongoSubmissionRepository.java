package ru.hse.rekoder.repositories.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.SubmissionRepository;

import java.util.Optional;

@Repository
public class MongoSubmissionRepository implements SubmissionRepository {
    private final MongoOperations mongoOperations;
    private int id = 0;

    public MongoSubmissionRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Optional<Submission> findById(Integer id) {
        return Optional.ofNullable(mongoOperations.findById(id, Submission.class, "submission"));
    }

    @Override
    public Submission save(Submission newSubmission) {
        if (newSubmission.getId() == null) {
            newSubmission.setId(id);
            ++id;
        }
        return mongoOperations.save(newSubmission, "submission");
    }
}
