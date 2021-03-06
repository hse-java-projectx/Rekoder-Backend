package ru.hse.rekoder.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.model.User;

import java.util.List;

public interface TeamService {
    Team getTeam(String teamName);
    Team createTeam(Team team, String founderUsername);
    Team updateTeam(Team team);

    List<Team> getTeamsUserIn(String userName);

    List<User> getAllMembers(String teamName);
    boolean addExistingUserToTeam(String teamName, String userName);
    boolean deleteUserFromTeam(String teamName, String userName);

    Page<Problem> getAllProblems(String teamName, Pageable pageable);
    Problem createProblem(String teamName, Problem problem);
    Problem cloneProblem(String ownerOfProblemClone, int originalProblemId);
}
