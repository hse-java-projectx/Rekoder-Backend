package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.Problem;

import java.util.Optional;

public interface ProblemRepository {
    Optional<Problem> findById(Integer id);
    Problem save(Problem newProblem);
    long count();
}
