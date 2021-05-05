package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemOwnerNotFoundException;
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

    public UserServiceImpl(UserRepository userRepository,
                           ProblemRepository problemRepository,
                           SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(int userId) throws ProblemOwnerNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with id \"" + userId + "\" not found"));
    }

    @Override
    public List<Problem> getProblems(int id) throws ProblemOwnerNotFoundException {
        return userRepository.findById(id)
                .map(User::getProblems)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("User with id \"" + id + "\" not found"));
    }

    @Override
    public User createUser(User user) {
        user.setId(null);
        user.setRegistrationTime(new Date());
        return userRepository.save(user);
    }
}
