package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemOwnerNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.TeamRepository;
import ru.hse.rekoder.repositories.UserRepository;
import ru.hse.rekoder.repositories.mongodb.seqGenerators.DatabaseIntSequenceService;

import java.util.Date;
import java.util.List;

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
        Folder rootFolder = new Folder();
        rootFolder.setOwnerId(new Team.TeamCompositeKey(team.getName()));
        rootFolder.setName("root");
        rootFolder.setId(sequenceService.generateSequence(Folder.SEQUENCE_NAME));
        rootFolder = folderRepository.save(rootFolder);
        team.setRootFolder(rootFolder);
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
    public Folder getRootFolder(String teamName) {
        return teamRepository.findById(teamName)
                .map(ProblemOwner::getRootFolder)
                .orElseThrow();
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
        problem.setOwnerId(new Team.TeamCompositeKey(teamName));
        problem.setId(sequenceService.generateSequence(Problem.SEQUENCE_NAME));
        problem = problemRepository.save(problem);
        team.getProblems().add(problem);
        teamRepository.save(team);
        return problem;
    }
}
