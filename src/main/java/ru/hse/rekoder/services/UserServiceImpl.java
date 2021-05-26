package ru.hse.rekoder.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.UserConflictException;
import ru.hse.rekoder.exceptions.UserException;
import ru.hse.rekoder.exceptions.UserNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final FolderRepository folderRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           ProblemRepository problemRepository,
                           FolderRepository folderRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.folderRepository = folderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
    }

    @Override
    public List<Problem> getProblems(String userName) {
        checkExistenceOfUser(userName);
        return problemRepository.findAllByOwner(createOwner(userName));
    }

    @Override
    public Problem createProblem(String userName, Problem problem) {
        checkExistenceOfUser(userName);
        problem.setOwner(createOwner(userName));
        problem = problemRepository.save(problem);
        return problem;
    }

    @Override
    public User createUser(User user) {
        user.setRegistrationDate(new Date());
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserConflictException(user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Folder rootFolder = new Folder();
        rootFolder.setOwner(createOwner(user.getUsername()));
        rootFolder.setName("root");
        rootFolder = folderRepository.save(rootFolder);
        user.setRootFolderId(rootFolder.getId());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        if (Objects.isNull(user.getId())) {
            throw new UserException("User must have an id");
        }
        return userRepository.update(user, user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));
    }

    private Owner createOwner(String username) {
        return new Owner(ContentGeneratorType.USER, username);
    }

    private void checkExistenceOfUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        }
    }
}
