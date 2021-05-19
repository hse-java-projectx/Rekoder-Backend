package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.ProblemOwner;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends MongoRepository<Problem, Integer> {
    Optional<Problem> findById(Integer id);
    List<Problem> findAllByOwnerId(ProblemOwner.CompositeKey id);
    List<Problem> findAllById(Iterable<Integer> id);
}
