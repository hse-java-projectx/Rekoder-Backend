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
    public Problem getProblem(int userId, int problemId) {
        Problem problem = problemRepository.findById(problemId).orElse(null);
        if (problem == null || problem.getOwner().getId() != userId) {
            return null;
        }
        return problem;
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

    @Override
    public Problem createNewProblem(int userId, Problem problem) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        problem.setId(null);//???
        problem.setOriginalProblem(null);
        problem.setOwner(user);
        problem = problemRepository.save(problem);
        user.getProblems().add(problem);
        return problem;
    }

    @Override
    public Problem copyProblem(int destUserId, int srcUserId, int srcProblemId) {
        return null;
    }

    @Override
    public Submission getSubmission(int userId, int problemId, int submissionId) {
        Submission submission = submissionRepository.findById(submissionId).orElse(null);
        if (submission == null
                || submission.getProblem().getId() != problemId
                || submission.getProblem().getOwner().getId() != userId) {
            return null;
        }
        return submission;
    }

    @Override
    public List<Submission> getAllSubmissions(int userId, int problemId) {
        Problem problem = problemRepository.findById(problemId).orElse(null);
        if (problem == null || problem.getOwner().getId() != userId) {
            return null;
        }
        return problem.getSubmissions();
    }

    @Override
    public Submission addSubmission(int userId, int problemId, Submission submission) {
        Problem problem = problemRepository.findById(problemId).orElse(null);
        if (problem == null || problem.getOwner().getId() != userId) {
            return null;
        }
        submission.setSubmissionTime(new Date());
        submission.setId(null);//???
        submission.setProblem(problem);
        submission = submissionRepository.save(submission);
        problem.getSubmissions().add(submission);
        return submission;
    }

}
