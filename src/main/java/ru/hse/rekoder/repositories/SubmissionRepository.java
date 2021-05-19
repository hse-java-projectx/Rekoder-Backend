package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.Submission;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends MongoRepository<Submission, Integer> {
    Optional<Submission> findById(Integer id);
    List<Submission> findAllByProblemId(Integer id);
}
