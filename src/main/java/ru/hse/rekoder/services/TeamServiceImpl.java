package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rekoder.exceptions.FolderNotFoundException;
import ru.hse.rekoder.exceptions.ProblemOwnerNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.TeamRepository;
import ru.hse.rekoder.repositories.UserRepository;
import ru.hse.rekoder.repositories.mongodb.seqGenerators.DatabaseIntSequenceService;

import java.nio.BufferUnderflowException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final FolderRepository folderRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final DatabaseIntSequenceService sequenceService;

    //TODO use TeamNotFoundException

    public TeamServiceImpl(TeamRepository teamRepository,
                           FolderRepository folderRepository,
                           ProblemRepository problemRepository,
                           UserRepository userRepository,
                           DatabaseIntSequenceService sequenceService) {
        this.teamRepository = teamRepository;
        this.folderRepository = folderRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public Team getTeam(String teamName) {
        return teamRepository.findById(new Team.TeamCompositeKey(teamName))
                .orElseThrow();
    }

    @Override
    public Team createTeam(Team team) {
        team.setId(new Team.TeamCompositeKey(team.getName()));
        if (teamRepository.existsById((Team.TeamCompositeKey) team.getId())) {
            throw new RuntimeException();
        }
        team.setRegistrationDate(new Date());
        Folder rootFolder = new Folder();
        rootFolder.setOwnerId(new Team.TeamCompositeKey(team.getName()));
        rootFolder.setName("root");
        rootFolder.setId(sequenceService.generateSequence(Folder.SEQUENCE_NAME));
        rootFolder = folderRepository.save(rootFolder);
        team.setRootFolderId(rootFolder.getId());
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Team team) {
        if (Objects.isNull(team.getId())) {
            throw new RuntimeException("Team must have an id");
        }
        return teamRepository.save(team);
    }

    @Override
    public List<Team> getTeamsUserIn(String userName) {
        return teamRepository.findAllByMemberIds(userName);
    }

    @Override
    public List<User> getAllMembers(String teamName) {
        Team team = teamRepository.findById(new Team.TeamCompositeKey(teamName))
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Team not found"));
        return userRepository.findAllById(team.getMemberIds()
                .stream()
                .map(User.UserCompositeKey::new)
                .collect(Collectors.toList()));
    }

    @Override
    public boolean addExistingUserToTeam(String teamName, String userName) {
        Team.TeamCompositeKey teamId = new Team.TeamCompositeKey(teamName);
        User.UserCompositeKey userId = new User.UserCompositeKey(userName);
        if (!userRepository.existsById(userId)) {
            throw new ProblemOwnerNotFoundException("User not found");
        }
        return teamRepository.addUserToTeamById(teamId, userName)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Team not found"));
    }

    @Override
    public boolean deleteUserFromTeam(String teamName, String userName) {
        Team.TeamCompositeKey teamId = new Team.TeamCompositeKey(teamName);
        User.UserCompositeKey userId = new User.UserCompositeKey(userName);
        if (!userRepository.existsById(userId)) {
            throw new ProblemOwnerNotFoundException("User not found");
        }
        return teamRepository.deleteUserFromTeamById(teamId, userName)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Team not found"));
    }

    @Override
    public List<Problem> getAllProblems(String teamName) {
        return problemRepository.findAllByOwnerId(new Team.TeamCompositeKey(teamName));
    }

    @Override
    public Problem createProblem(String teamName, Problem problem) {
        Team team = teamRepository.findById(new Team.TeamCompositeKey(teamName))
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Team not found"));
        problem.setOwnerId(new Team.TeamCompositeKey(teamName));
        problem.setId(sequenceService.generateSequence(Problem.SEQUENCE_NAME));
        problem = problemRepository.save(problem);
        return problem;
    }
}
