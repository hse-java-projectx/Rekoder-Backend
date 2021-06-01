package ru.hse.rekoder.services;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemException;
import ru.hse.rekoder.exceptions.ProblemNotFoundException;
import ru.hse.rekoder.model.Owner;
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
        if (!problemRepository.existsById(problemId)) {
            submissionRepository.deleteById(submission.getId());
            throw new ProblemNotFoundException(problemId);
        }
        return submission;
    }

    @Override
    public Problem updateProblem(Problem problem) {
        if (Objects.isNull(problem.getId())) {
            throw new ProblemException("Problem must have an id");
        }
        return problemRepository.update(problem, problem.getId())
                .orElseThrow(() -> new ProblemNotFoundException(problem.getId()));
    }

    @Override
    public Problem createProblem(Problem problem) {
        problem.setId(null);
        return problemRepository.save(problem);
    }

    @Override
    public Problem cloneProblem(Owner ownerOfProblemClone, int originalProblemId) {
        Problem originalProblem = problemRepository.findById(originalProblemId)
                .orElseThrow(() -> new ProblemNotFoundException(originalProblemId));
        Problem problemClone = new Problem();
        BeanUtils.copyProperties(originalProblem, problemClone);
        problemClone.setId(null);
        problemClone.setOwner(ownerOfProblemClone);
        if (Objects.isNull(originalProblem.getOriginalProblemId())) {
            problemClone.setOriginalProblemId(originalProblemId);
        } else {
            problemClone.setOriginalProblemId(originalProblem.getOriginalProblemId());
        }
        return problemRepository.save(problemClone);
    }

    @Override
    public List<Problem> getAllProblemsOfOwner(Owner owner) {
        return problemRepository.findAllByOwner(owner);
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
