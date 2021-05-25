package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.model.User;

import java.util.List;

public interface ProblemService {
    Problem getProblem(int problemId);

    List<Submission> getAllSubmissions(int problemId);
    Submission createSubmission(int problemId, Submission submission, String author);

    Problem updateProblem(Problem problem);

    void deleteProblem(int problemId);
}
