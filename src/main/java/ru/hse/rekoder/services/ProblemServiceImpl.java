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
import java.util.Objects;

@Service
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final DatabaseIntSequenceService sequenceService;

    public ProblemServiceImpl(ProblemRepository problemRepository,
                              SubmissionRepository submissionRepository,
                              DatabaseIntSequenceService sequenceService,
                              JsonMergePatchService jsonMergePatchService) {
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
        submission.setAuthorId(problem.getOwner().getId());
        submission.setId(sequenceService.generateSequence(Submission.SEQUENCE_NAME));
        submission = submissionRepository.save(submission);
        return submission;
    }

    @Override
    public Problem updateProblem(Problem problem) {
        if (Objects.isNull(problem.getId())) {
            throw new RuntimeException("Problem must have an id");
        }
        //use update
        return problemRepository.save(problem);
    }

    @Override
    public void deleteProblem(int problemId) {
        if (!problemRepository.existsById(problemId)) {
            throw new ProblemNotFoundException("Problem not found");
        }
        problemRepository.deleteById(problemId);
        submissionRepository.deleteAllByProblemId(problemId);
    }
}
