package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.model.User;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

public interface TeamService {
    Team getTeam(String teamName);
    Team createTeam(Team team);

    List<User> getAllMembers(String teamName);
    Team addExistingUsers(String teamName, String userId);

    List<Folder> getTopFolders(String teamName);
    Folder createTopFolder(String teamName, Folder folder);

    List<Problem> getAllProblems(String teamName);
    Problem createProblem(String teamName, Problem problem);
}
