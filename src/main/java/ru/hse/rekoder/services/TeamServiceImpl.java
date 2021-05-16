package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemOwnerNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.TeamRepository;
import ru.hse.rekoder.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final FolderRepository folderRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    //TODO use TeamNotFoundException

    public TeamServiceImpl(TeamRepository teamRepository,
                           FolderRepository folderRepository,
                           ProblemRepository problemRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.folderRepository = folderRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Team getTeam(String teamName) {
        return teamRepository.findById(teamName)
                .orElseThrow();
    }

    @Override
    public Team createTeam(Team team) {
        if (teamRepository.exists(team.getName())) {
            throw new RuntimeException();
        }
        team.setRegistrationDate(new Date());
        team.setId(null);
        return teamRepository.save(team);
    }

    @Override
    public List<User> getAllMembers(String teamName) {
        return teamRepository.findById(teamName)
                .map(Team::getMembers)
                .orElseThrow();
    }

    @Override
    public Team addExistingUsers(String teamName, String userId) {
        Team team = teamRepository.findById(teamName)
                .orElseThrow();
        //TODO maybe check that user.getId() != null
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with name \"" + userId + "\" not found"));
        team.getMembers().add(user);
        user.getTeams().add(team);
        userRepository.save(user);
        teamRepository.save(team);
        return team;
    }

    @Override
    public List<Folder> getTopFolders(String teamName) {
        return teamRepository.findById(teamName)
                .map(ProblemOwner::getTopFolders)
                .orElseThrow();
    }

    @Override
    public Folder createTopFolder(String teamName, Folder folder) {
        Team team = teamRepository.findById(teamName)
                .orElseThrow();
        folder.setParentFolder(null);
        folder.setId(null);
        folder.setOwner(team);
        folder = folderRepository.save(folder);
        team.getTopFolders().add(folder);
        teamRepository.save(team);
        return folder;
    }

    @Override
    public List<Problem> getAllProblems(String teamName) {
        return teamRepository.findById(teamName)
                .map(ProblemOwner::getProblems)
                .orElseThrow();
    }

    @Override
    public Problem createProblem(String teamName, Problem problem) {
        Team team = teamRepository.findById(teamName)
                .orElseThrow();
        problem.setId(null);
        problem.setOriginalProblem(null);
        problem.setOwner(team);
        problem = problemRepository.save(problem);
        team.getProblems().add(problem);
        teamRepository.save(team);
        return problem;
    }
}
