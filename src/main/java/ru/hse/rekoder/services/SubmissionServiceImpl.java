package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.SubmissionRepository;

import java.util.Date;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
                                 ProblemRepository problemRepository) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
    }

    @Override
    public Submission getSubmission(int submissionId) {
        return submissionRepository.findById(submissionId).orElse(null);
    }

    @Override
    public Submission addSubmission(int problemId, Submission submission) {
        Problem problem = problemRepository.findById(problemId).orElse(null);
        if (problem == null) {
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
