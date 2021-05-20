package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;

import javax.json.JsonMergePatch;
import java.util.List;

public interface ProblemService {
    Problem getProblem(int problemId);

    List<Submission> getAllSubmissions(int problemId);
    Submission createSubmission(int problemId, Submission submission);

    Problem updateProblem(int problemId, JsonMergePatch jsonMergePatch);

    void deleteProblem(int problemId);
}
