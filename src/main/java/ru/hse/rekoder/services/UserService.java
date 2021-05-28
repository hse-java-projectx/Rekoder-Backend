package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.User;

import java.util.List;

public interface UserService {
    User getUser(String userName);
    User createUser(User user);
    User updateUser(User user);

    List<Problem> getProblems(String userName);
    Problem createProblem(String userName, Problem problem);
}
