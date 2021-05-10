package ru.hse.rekoder.repositories.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.SubmissionRepository;
import ru.hse.rekoder.repositories.mongodb.seqGenerators.DatabaseIntSequenceService;

import java.util.Optional;

@Repository
public class MongoSubmissionRepository implements SubmissionRepository {
    private static final String COLLECTION_NAME = "submissions";
    private static final String SEQUENCE_NAME = "submission_sequence";

    private final MongoOperations mongoOperations;
    private final DatabaseIntSequenceService sequenceGeneratorService;

    public MongoSubmissionRepository(MongoOperations mongoOperations,
                                     DatabaseIntSequenceService sequenceGeneratorService) {
        this.mongoOperations = mongoOperations;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public Optional<Submission> findById(Integer id) {
        return Optional.ofNullable(mongoOperations.findById(id, Submission.class, COLLECTION_NAME));
    }

    @Override
    public Submission save(Submission newSubmission) {
        if (newSubmission.getId() == null) {
            newSubmission.setId(sequenceGeneratorService.generateSequence(SEQUENCE_NAME));
        }
        return mongoOperations.save(newSubmission, COLLECTION_NAME);
    }
}
