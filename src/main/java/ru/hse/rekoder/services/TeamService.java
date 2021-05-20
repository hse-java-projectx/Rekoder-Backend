package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.model.User;

import java.util.List;

public interface TeamService {
    Team getTeam(String teamName);
    Team createTeam(Team team);
    Team updateTeam(Team team);

    List<User> getAllMembers(String teamName);
    Team addExistingUsers(String teamName, String userId);

    List<Problem> getAllProblems(String teamName);
    Problem createProblem(String teamName, Problem problem);
}
