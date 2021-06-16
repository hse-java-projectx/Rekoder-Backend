package ru.hse.rekoder.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.TeamConflictException;
import ru.hse.rekoder.exceptions.TeamException;
import ru.hse.rekoder.exceptions.TeamNotFoundException;
import ru.hse.rekoder.exceptions.UserNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.TeamRepository;
import ru.hse.rekoder.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final FolderService folderService;
    private final ProblemService problemService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           UserRepository userRepository,
                           FolderService folderService,
                           ProblemService problemService) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.folderService = folderService;
        this.problemService = problemService;
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
            throw new TeamException("The team must have an id");
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
        if (getTeam(teamId).getMemberIds().size() > Team.MAX_MEMBERS_NUMBER) {
            throw new TeamException("Too many members in the team. " +
                    "The number of people in the team should not exceed " + Team.MAX_MEMBERS_NUMBER);
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
    public Page<Problem> getAllProblems(String teamId, Pageable pageable) {
        checkExistenceOfTeam(teamId);
        return problemService.getAllProblemsOfOwner(Owner.teamWithId(teamId), pageable);
    }

    @Override
    public Problem createProblem(String teamId, Problem problem) {
        checkExistenceOfTeam(teamId);
        problem.setOwner(Owner.teamWithId(teamId));
        return problemService.createProblem(problem);
    }

    @Override
    public Problem cloneProblem(String ownerOfProblemClone, int originalProblemId) {
        checkExistenceOfTeam(ownerOfProblemClone);
        return problemService.cloneProblem(Owner.teamWithId(ownerOfProblemClone), originalProblemId);
    }

    private void checkExistenceOfTeam(String teamId) {
        if (!teamRepository.existsByTeamId(teamId)) {
            throw new TeamNotFoundException(teamId);
        }
    }
}
