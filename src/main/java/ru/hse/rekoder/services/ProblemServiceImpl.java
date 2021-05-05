package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.ProblemNotFoundException;
import ru.hse.rekoder.exceptions.ProblemOwnerNotFoundException;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.ProblemOwner;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.ProblemOwnerRepository;
import ru.hse.rekoder.repositories.ProblemRepository;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemOwnerRepository ownerRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository,
                              ProblemOwnerRepository ownerRepository) {
        this.problemRepository = problemRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Problem getProblem(int problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Problem with id \"" + problemId + "\" not found"));
    }

    @Override
    public Problem createNewProblem(int ownerId, Problem problem) {
        ProblemOwner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ProblemOwnerNotFoundException("Problem owner with id \"" + ownerId + "\" not found"));
        problem.setId(null);//???
        problem.setOriginalProblem(null);
        problem.setOwner(owner);
        problem = problemRepository.save(problem);
        owner.getProblems().add(problem);
        return problem;
    }

    @Override
    public Problem copyProblem(int destOwnerId, int srcOwnerId, int srcProblemId) {
        return null;
    }

    @Override
    public List<Submission> getAllSubmissions(int problemId) {
        return problemRepository.findById(problemId)
                .map(Problem::getSubmissions)
                .orElseThrow(() -> new ProblemNotFoundException("Problem with id \"" + problemId + "\" not found"));
    }
}
