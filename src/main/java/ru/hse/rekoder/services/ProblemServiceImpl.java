package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemNotFoundException;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.SubmissionRepository;

import java.util.Date;
import java.util.List;

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
                .orElseThrow(() -> new ProblemNotFoundException("Problem with id \"" + problemId + "\" not found"));
    }

    @Override
    public List<Submission> getAllSubmissions(int problemId) {
        return problemRepository.findById(problemId)
                .map(Problem::getSubmissions)
                .orElseThrow(() -> new ProblemNotFoundException("Problem with id \"" + problemId + "\" not found"));
    }

    @Override
    public Submission createSubmission(int problemId, Submission submission) {
        submission.setSubmissionTime(new Date());
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Problem with id \"" + problemId + "\" not found"));
        submission.setId(null);
        submission.setProblem(problem);
        submission.setAuthor(problem.getOwner());
        submission = submissionRepository.save(submission);
        problem.getSubmissions().add(submission);
        problemRepository.save(problem);
        return submission;
    }
}
