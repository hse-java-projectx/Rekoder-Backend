package ru.hse.rekoder.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;

public interface ProblemService {
    Problem getProblem(int problemId);

    Page<Submission> getAllSubmissions(int problemId, Pageable pageable);
    Submission createSubmission(int problemId, Submission submission, String author);

    Problem updateProblem(Problem problem);
    Problem createProblem(Problem problem);
    Problem cloneProblem(Owner ownerOfProblemClone, int originalProblemId);

    Page<Problem> getAllProblemsOfOwner(Owner owner, Pageable pageable);

    void deleteProblem(int problemId);
}
