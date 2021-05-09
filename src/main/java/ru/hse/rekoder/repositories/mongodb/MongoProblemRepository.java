package ru.hse.rekoder.repositories.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.repositories.ProblemRepository;

import java.util.Optional;

@Repository
public class MongoProblemRepository implements ProblemRepository {
    private final MongoOperations mongoOperations;
    private int id = 0;

    public MongoProblemRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Optional<Problem> findById(Integer id) {
        return Optional.ofNullable(mongoOperations.findById(id, Problem.class, "problem"));
    }

    @Override
    public Problem save(Problem newProblem) {
        if (newProblem.getId() == null) {
            newProblem.setId(id);
            ++id;
        }
        return mongoOperations.save(newProblem, "problem");
    }

    @Override
    public long count() {
        return 0;
    }
}
