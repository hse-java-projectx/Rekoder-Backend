package ru.hse.rekoder.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.UserConflictException;
import ru.hse.rekoder.exceptions.UserException;
import ru.hse.rekoder.exceptions.UserNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.PasswordRepository;
import ru.hse.rekoder.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProblemService problemService;
    private final FolderService folderService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordRepository passwordRepository;

    public UserServiceImpl(UserRepository userRepository,
                           ProblemService problemService,
                           FolderService folderService,
                           PasswordEncoder passwordEncoder,
                           PasswordRepository passwordRepository) {
        this.userRepository = userRepository;
        this.problemService = problemService;
        this.folderService = folderService;
        this.passwordEncoder = passwordEncoder;
        this.passwordRepository = passwordRepository;
    }

    @Override
    public User getUser(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
    }

    @Override
    public Page<Problem> getProblems(String userName, Pageable pageable) {
        checkExistenceOfUser(userName);
        return problemService.getAllProblemsOfOwner(Owner.userWithId(userName), pageable);
    }

    @Override
    public Problem createProblem(String userName, Problem problem) {
        checkExistenceOfUser(userName);
        problem.setOwner(Owner.userWithId(userName));
        return problemService.createProblem(problem);
    }

    @Override
    public Problem cloneProblem(String cloneOwner, int originalProblemId) {
        checkExistenceOfUser(cloneOwner);
        return problemService.cloneProblem(Owner.userWithId(cloneOwner), originalProblemId);
    }

    @Override
    public User createUser(User user) {
        user.setRegistrationDate(new Date());
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserConflictException(user.getUsername());
        }

        user.getPassword().setStringPassword(passwordEncoder.encode(user.getPassword().getStringPassword()));
        user.getPassword().setId(null);
        user.setPassword(passwordRepository.save(user.getPassword()));

        Folder rootFolder = new Folder();
        rootFolder.setOwner(Owner.userWithId(user.getUsername()));
        rootFolder.setName("root");
        rootFolder = folderService.createRootFolder(rootFolder);

        user.setRootFolderId(rootFolder.getId());
        try {
            return userRepository.insert(user);
        } catch (DuplicateKeyException e) {
            folderService.deleteFolder(rootFolder.getId());
            throw new UserConflictException(user.getUsername());
        }
    }

    @Override
    public User updateUser(User user) {
        if (Objects.isNull(user.getId())) {
            throw new UserException("User must have an id");
        }
        return userRepository.update(user, user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));
    }

    @Override
    public void changePassword(String username, String password) {
        Password userPassword = getUser(username).getPassword();
        userPassword.setStringPassword(passwordEncoder.encode(password));
        passwordRepository.save(userPassword);
    }

    private void checkExistenceOfUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        }
    }
}
