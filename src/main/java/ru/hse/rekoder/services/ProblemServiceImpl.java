package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemException;
import ru.hse.rekoder.exceptions.ProblemNotFoundException;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.SubmissionRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository,
                              SubmissionRepository submissionRepository) {
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public Problem getProblem(int problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException(problemId));
    }

    @Override
    public List<Submission> getAllSubmissions(int problemId) {
        checkExistenceOfProblem(problemId);
        return submissionRepository.findAllByProblemId(problemId);
    }

    @Override
    public Submission createSubmission(int problemId, Submission submission, String author) {
        submission.setSubmissionTime(new Date());
        checkExistenceOfProblem(problemId);
        submission.setProblemId(problemId);
        submission.setAuthorId(author);
        submission = submissionRepository.save(submission);
        return submission;
    }

    @Override
    public Problem updateProblem(Problem problem) {
        if (Objects.isNull(problem.getId())) {
            throw new ProblemException("Problem must have an id");
        }
        //use update
        return problemRepository.save(problem);
    }

    @Override
    public void deleteProblem(int problemId) {
        checkExistenceOfProblem(problemId);
        problemRepository.deleteById(problemId);
        submissionRepository.deleteAllByProblemId(problemId);
    }

    private void checkExistenceOfProblem(int problemId) {
        if (!problemRepository.existsById(problemId)) {
            throw new ProblemNotFoundException(problemId);
        }
    }
}
