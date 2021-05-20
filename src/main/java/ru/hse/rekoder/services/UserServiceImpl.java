package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.FolderNotFoundException;
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

    public UserServiceImpl(UserRepository userRepository,
                           ProblemRepository problemRepository,
                           FolderRepository folderRepository,
                           DatabaseIntSequenceService sequenceService) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.folderRepository = folderRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public User getUser(String userName) throws ProblemOwnerNotFoundException {
        return userRepository.findById(new User.UserCompositeKey(userName))
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with id \"" + userName + "\" not found"));
    }

    @Override
    public List<Problem> getProblems(String userName) throws ProblemOwnerNotFoundException {
        return problemRepository.findAllByOwnerId(new User.UserCompositeKey(userName));
    }

    @Override
    public Problem createProblem(String userName, Problem problem) {
        User user = userRepository.findById(new User.UserCompositeKey(userName))
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with name \"" + userName + "\" not found"));
        problem.setOwnerId(new User.UserCompositeKey(userName));
        problem.setId(sequenceService.generateSequence(Problem.SEQUENCE_NAME));
        problem = problemRepository.save(problem);
        return problem;
    }

    @Override
    public User createUser(User user) {
        user.setId(new User.UserCompositeKey(user.getName()));
        if (userRepository.existsById((User.UserCompositeKey) user.getId())) {
            throw new RuntimeException("User has already existed");
        }
        user.setRegistrationTime(new Date());
        Folder rootFolder = new Folder();
        rootFolder.setOwnerId(new User.UserCompositeKey(user.getName()));
        rootFolder.setName("root");
        rootFolder.setId(sequenceService.generateSequence(Folder.SEQUENCE_NAME));
        rootFolder = folderRepository.save(rootFolder);
        user.setRootFolderId(rootFolder.getId());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        if (Objects.isNull(user.getId())) {
            throw new RuntimeException("User must have an id");
        }
        return userRepository.save(user);
    }
}
