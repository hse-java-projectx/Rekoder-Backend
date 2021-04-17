package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.model.User;

import java.util.List;

public interface UserService {
    //so far, the methods return null if something went wrong

    User getUser(int userId);
    User addUser(User user);

    Problem getProblem(int userId, int problemId);
    List<Problem> getProblems(int userId);
    Problem createNewProblem(int userId, Problem problem);
    Problem copyProblem(int destUserId, int srcUserId, int srcProblemId);

    Submission getSubmission(int userId, int problemId, int submissionId);
    List<Submission> getAllSubmissions(int userId, int problemId);
    Submission addSubmission(int userId, int problemId, Submission submission);
}
