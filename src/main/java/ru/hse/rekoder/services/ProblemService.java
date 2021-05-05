package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;

import java.util.List;

public interface ProblemService {
    Problem getProblem(int problemId);
    Problem createNewProblem(int ownerId, Problem problem);
    Problem copyProblem(int destOwnerId, int srcOwnerId, int srcProblemId);
    List<Submission> getAllSubmissions(int problemId);
}
