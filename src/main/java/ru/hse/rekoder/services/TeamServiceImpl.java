package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.TeamConflictException;
import ru.hse.rekoder.exceptions.TeamException;
import ru.hse.rekoder.exceptions.TeamNotFoundException;
import ru.hse.rekoder.exceptions.UserNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.TeamRepository;
import ru.hse.rekoder.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final FolderRepository folderRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    public TeamServiceImpl(TeamRepository teamRepository,
                           FolderRepository folderRepository,
                           ProblemRepository problemRepository,
                           UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.folderRepository = folderRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Team getTeam(String teamName) {
        return teamRepository.findByTeamId(teamName)
                .orElseThrow(() -> new TeamNotFoundException(teamName));
    }

    @Override
    public Team createTeam(Team team, String founderUsername) {
        team.setRegistrationDate(new Date());
        if (teamRepository.existsByTeamId(team.getTeamId())) {
            throw new TeamConflictException(team.getTeamId());
        }
        team.getMemberIds().add(founderUsername);
        Folder rootFolder = new Folder();
        rootFolder.setOwner(createOwner(team.getTeamId()));
        rootFolder.setName("root");
        rootFolder = folderRepository.save(rootFolder);
        team.setRootFolderId(rootFolder.getId());
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Team team) {
        if (Objects.isNull(team.getId())) {
            throw new TeamException("Team must have an id");
        }
        return teamRepository.update(team, team.getId())
                .orElseThrow(() -> new TeamNotFoundException(team.getTeamId()));
    }

    @Override
    public List<Team> getTeamsUserIn(String userName) {
        if (!userRepository.existsByUsername(userName)) {
            throw new UserNotFoundException(userName);
        }
        return teamRepository.findAllByMemberIds(userName);
    }

    @Override
    public List<User> getAllMembers(String teamName) {
        Team team = teamRepository.findByTeamId(teamName)
                .orElseThrow(() -> new TeamNotFoundException(teamName));
        return userRepository.findAllByUsername(team.getMemberIds());
    }

    @Override
    public boolean addExistingUserToTeam(String teamId, String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        }
        return teamRepository.addUserToTeamById(teamId, username)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
    }

    @Override
    public boolean deleteUserFromTeam(String teamId, String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        }
        return teamRepository.deleteUserFromTeamById(teamId, username)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
    }

    @Override
    public List<Problem> getAllProblems(String teamId) {
        checkExistenceOfTeam(teamId);
        return problemRepository.findAllByOwner(createOwner(teamId));
    }

    @Override
    public Problem createProblem(String teamId, Problem problem) {
        checkExistenceOfTeam(teamId);
        problem.setOwner(createOwner(teamId));
        problem = problemRepository.save(problem);
        return problem;
    }

    private Owner createOwner(String teamId) {
        return new Owner(ContentGeneratorType.TEAM, teamId);
    }

    private void checkExistenceOfTeam(String teamId) {
        if (!teamRepository.existsByTeamId(teamId)) {
            throw new TeamNotFoundException(teamId);
        }
    }
}
