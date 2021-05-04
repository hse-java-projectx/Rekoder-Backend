package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.SubmissionRepository;
import ru.hse.rekoder.repositories.UserRepository;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;

    public UserServiceImpl(UserRepository userRepository,
                           ProblemRepository problemRepository,
                           SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public User getUser(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<Problem> getProblems(int id) {
        return userRepository.findById(id).map(User::getProblems).orElse(null);
    }

    @Override
    public User addUser(User user) {
        user.setId(null);
        user.setRegistrationTime(new Date());
        return userRepository.save(user);
    }
}
