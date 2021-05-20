package ru.hse.rekoder.services;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemNotFoundException;
import ru.hse.rekoder.exceptions.ProblemOwnerNotFoundException;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.SubmissionRepository;
import ru.hse.rekoder.repositories.mongodb.seqGenerators.DatabaseIntSequenceService;
import ru.hse.rekoder.requests.PatchingProblem;

import javax.json.JsonMergePatch;
import java.util.Date;
import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final DatabaseIntSequenceService sequenceService;
    private final JsonMergePatchService jsonMergePatchService;

    public ProblemServiceImpl(ProblemRepository problemRepository,
                              SubmissionRepository submissionRepository,
                              DatabaseIntSequenceService sequenceService,
                              JsonMergePatchService jsonMergePatchService) {
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
        this.sequenceService = sequenceService;
        this.jsonMergePatchService = jsonMergePatchService;
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

    @Override
    public Problem updateProblem(int problemId, JsonMergePatch jsonMergePatch) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Problem not found"));

        PatchingProblem patchingProblem = new PatchingProblem(problem);

        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, patchingProblem, PatchingProblem.class),
                problem);

        problem = problemRepository.save(problem);
        return problem;
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
