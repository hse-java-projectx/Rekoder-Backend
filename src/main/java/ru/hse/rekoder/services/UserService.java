package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.model.User;

import java.util.List;

public interface UserService {
    //so far, the methods return null if something went wrong

    User getUser(String userName);
    User createUser(User user);

    List<Problem> getProblems(String userName);
    Problem createProblem(String userName, Problem problem);

    List<Folder> getTopFolder(String userName);
    Folder createTopFolder(String userName, Folder folder);
}
