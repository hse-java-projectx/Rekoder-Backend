package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemOwnerNotFoundException;
import ru.hse.rekoder.model.*;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.UserRepository;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final FolderRepository folderRepository;

    public UserServiceImpl(UserRepository userRepository,
                           ProblemRepository problemRepository,
                           FolderRepository folderRepository) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.folderRepository = folderRepository;
    }

    @Override
    public User getUser(String userName) throws ProblemOwnerNotFoundException {
        return userRepository.findById(userName)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with id \"" + userName + "\" not found"));
    }

    @Override
    public List<Problem> getProblems(String userName) throws ProblemOwnerNotFoundException {
        return userRepository.findById(userName)
                .map(User::getProblems)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with id \"" + userName + "\" not found"));
    }

    @Override
    public Problem createProblem(String userName, Problem problem) {
        User user = userRepository.findById(userName)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with name \"" + userName + "\" not found"));
        problem.setOwner(user);
        problem.setId(null);//??
        problem = problemRepository.save(problem);
        user.getProblems().add(problem);
        userRepository.save(user);
        return problem;
    }

    @Override
    public Folder getRootFolder(String userName) {
        return userRepository.findById(userName)
                .map(ProblemOwner::getRootFolder)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with name \"" + userName + "\" not found"));

    }

    @Override
    public User createUser(User user) {
        if (userRepository.exists(user.getName())) {
            throw new RuntimeException("User has already existed");
        }
        user.setId(null);
        user.setRegistrationTime(new Date());
        Folder rootFolder = new Folder();
        rootFolder.setOwner(user);
        rootFolder.setName("root");
        rootFolder = folderRepository.save(rootFolder);
        user.setRootFolder(rootFolder);
        return userRepository.save(user);
    }
}
