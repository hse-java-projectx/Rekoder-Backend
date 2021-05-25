package ru.hse.rekoder.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemOwnerNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.UserRepository;
import ru.hse.rekoder.repositories.mongodb.seqGenerators.DatabaseIntSequenceService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final FolderRepository folderRepository;
    private final DatabaseIntSequenceService sequenceService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           ProblemRepository problemRepository,
                           FolderRepository folderRepository,
                           DatabaseIntSequenceService sequenceService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.folderRepository = folderRepository;
        this.sequenceService = sequenceService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(String userName) throws ProblemOwnerNotFoundException {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with id \"" + userName + "\" not found"));
    }

    @Override
    public List<Problem> getProblems(String userName) throws ProblemOwnerNotFoundException {
        return problemRepository.findAllByOwner(createOwner(userName));
    }

    @Override
    public Problem createProblem(String userName, Problem problem) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with name \"" + userName + "\" not found"));
        problem.setOwner(createOwner(userName));
        problem.setId(sequenceService.generateSequence(Problem.SEQUENCE_NAME));
        problem = problemRepository.save(problem);
        return problem;
    }

    @Override
    public User createUser(User user) {
        user.setRegistrationDate(new Date());
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User has already existed");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Folder rootFolder = new Folder();
        rootFolder.setOwner(createOwner(user.getUsername()));
        rootFolder.setName("root");
        rootFolder.setId(sequenceService.generateSequence(Folder.SEQUENCE_NAME));
        rootFolder = folderRepository.save(rootFolder);
        user.setRootFolderId(rootFolder.getId());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        if (Objects.isNull(user.getObjectId())) {
            throw new RuntimeException("User must have an id");
        }
        return userRepository.save(user);
    }

    private Owner createOwner(String username) {
        return new Owner(ContentGeneratorType.USER, username);
    }
}
