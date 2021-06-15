package ru.hse.rekoder.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Submission;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends MongoRepository<Submission, Integer>, UpdatableRepository<Submission, Integer> {
    Optional<Submission> findById(Integer id);
    Page<Submission> findAllByProblemId(Integer id, Pageable pageable);
    void deleteAllByProblemId(Integer id);
}
