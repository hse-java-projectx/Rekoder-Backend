package ru.hse.rekoder.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.TeamConflictException;
import ru.hse.rekoder.exceptions.TeamException;
import ru.hse.rekoder.exceptions.TeamNotFoundException;
import ru.hse.rekoder.exceptions.UserNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.TeamRepository;
import ru.hse.rekoder.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final FolderService folderService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           ProblemRepository problemRepository,
                           UserRepository userRepository,
                           FolderService folderService) {
        this.teamRepository = teamRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.folderService = folderService;
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
        rootFolder.setOwner(Owner.teamWithId(team.getTeamId()));
        rootFolder.setName("root");
        rootFolder = folderService.createRootFolder(rootFolder);

        team.setRootFolderId(rootFolder.getId());
        try {
            return teamRepository.insert(team);
        } catch (DuplicateKeyException e) {
            folderService.deleteFolder(rootFolder.getId());
            throw new TeamConflictException(team.getTeamId());
        }
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
        return userRepository.findAllByUsernameIn(team.getMemberIds());
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
        return problemRepository.findAllByOwner(Owner.teamWithId(teamId));
    }

    @Override
    public Problem createProblem(String teamId, Problem problem) {
        checkExistenceOfTeam(teamId);
        problem.setOwner(Owner.teamWithId(teamId));
        problem = problemRepository.save(problem);
        return problem;
    }

    private void checkExistenceOfTeam(String teamId) {
        if (!teamRepository.existsByTeamId(teamId)) {
            throw new TeamNotFoundException(teamId);
        }
    }
}
