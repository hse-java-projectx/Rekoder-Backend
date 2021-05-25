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
        return teamRepository.findByTeamId(teamName)
                .orElseThrow();
    }

    @Override
    public Team createTeam(Team team) {
        team.setRegistrationDate(new Date());
        if (teamRepository.existsByTeamId(team.getTeamId())) {
            throw new RuntimeException();
        }
        Folder rootFolder = new Folder();
        rootFolder.setOwner(createOwner(team.getTeamId()));
        rootFolder.setName("root");
        rootFolder.setId(sequenceService.generateSequence(Folder.SEQUENCE_NAME));
        rootFolder = folderRepository.save(rootFolder);
        team.setRootFolderId(rootFolder.getId());
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Team team) {
        if (Objects.isNull(team.getObjectId())) {
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
        Team team = teamRepository.findByTeamId(teamName)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Team not found"));
        return userRepository.findAllByUsername(team.getMemberIds());
    }

    @Override
    public boolean addExistingUserToTeam(String teamId, String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new ProblemOwnerNotFoundException("User not found");
        }
        return teamRepository.addUserToTeamById(teamId, username)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Team not found"));
    }

    @Override
    public boolean deleteUserFromTeam(String teamId, String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new ProblemOwnerNotFoundException("User not found");
        }
        return teamRepository.deleteUserFromTeamById(teamId, username)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Team not found"));
    }

    @Override
    public List<Problem> getAllProblems(String teamId) {
        return problemRepository.findAllByOwner(createOwner(teamId));
    }

    @Override
    public Problem createProblem(String teamId, Problem problem) {
        Team team = teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Team not found"));
        problem.setOwner(createOwner(teamId));
        problem.setId(sequenceService.generateSequence(Problem.SEQUENCE_NAME));
        problem = problemRepository.save(problem);
        return problem;
    }

    private Owner createOwner(String teamId) {
        return new Owner(ContentGeneratorType.TEAM, teamId);
    }
}
