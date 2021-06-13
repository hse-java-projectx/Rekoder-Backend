package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.User;

import java.util.List;

public interface UserService {
    User getUser(String userName);
    User createUser(User user);
    User updateUser(User user);

    void changePassword(String username, String password);

    List<Problem> getProblems(String userName);
    Problem createProblem(String userName, Problem problem);
    Problem cloneProblem(String cloneOwner, int originalProblemId);
}
