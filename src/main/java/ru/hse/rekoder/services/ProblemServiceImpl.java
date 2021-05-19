package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemNotFoundException;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.SubmissionRepository;
import ru.hse.rekoder.repositories.mongodb.seqGenerators.DatabaseIntSequenceService;

import java.util.Date;
import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final DatabaseIntSequenceService sequenceService;

    public ProblemServiceImpl(ProblemRepository problemRepository,
                              SubmissionRepository submissionRepository,
                              DatabaseIntSequenceService sequenceService) {
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public Problem getProblem(int problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Problem with id \"" + problemId + "\" not found"));
    }

    @Override
    public List<Submission> getAllSubmissions(int problemId) {
        return submissionRepository.findAllByProblemId(problemId);
    }

    @Override
    public Submission createSubmission(int problemId, Submission submission) {
        submission.setSubmissionTime(new Date());
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Problem with id \"" + problemId + "\" not found"));
        submission.setProblemId(problemId);
        //TODO
        submission.setAuthorId(problem.getOwnerId());
        submission.setId(sequenceService.generateSequence(Submission.SEQUENCE_NAME));
        submission = submissionRepository.save(submission);
        return submission;
    }
}
