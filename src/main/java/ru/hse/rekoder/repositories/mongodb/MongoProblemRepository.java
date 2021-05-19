package ru.hse.rekoder.repositories.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.mongodb.seqGenerators.DatabaseIntSequenceService;

import java.util.Optional;


public class MongoProblemRepository /*implements ProblemRepository*/ {
    private static final String COLLECTION_NAME = "problems";
    private static final String SEQUENCE_NAME = "problem_sequence";

    private final MongoOperations mongoOperations;
    private final DatabaseIntSequenceService sequenceGeneratorService;

    public MongoProblemRepository(MongoOperations mongoOperations,
                                  DatabaseIntSequenceService sequenceGeneratorService) {
        this.mongoOperations = mongoOperations;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    //@Override
    public Optional<Problem> findById(Integer id) {
        return Optional.ofNullable(mongoOperations.findById(id, Problem.class, COLLECTION_NAME));
    }

    //@Override
    public Problem save(Problem newProblem) {
        if (newProblem.getId() == null) {
            newProblem.setId(sequenceGeneratorService.generateSequence(SEQUENCE_NAME));
        }
        return mongoOperations.save(newProblem, COLLECTION_NAME);
    }

    //@Override
    public long count() {
        return mongoOperations.count(new Query(), Problem.class, COLLECTION_NAME);
    }
}
