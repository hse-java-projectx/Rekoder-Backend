package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.model.Problem;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends MongoRepository<Problem, Integer>, UpdatableRepository<Problem, Integer> {
    Optional<Problem> findById(Integer id);
    List<Problem> findAllByOwner(Owner owner);
    List<Problem> findAllById(Iterable<Integer> id);
}
