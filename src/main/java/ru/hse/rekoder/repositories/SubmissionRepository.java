package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.Submission;

import java.util.Optional;

public interface SubmissionRepository {
    Optional<Submission> findById(Integer id);
    Submission save(Submission newSubmission);
}
