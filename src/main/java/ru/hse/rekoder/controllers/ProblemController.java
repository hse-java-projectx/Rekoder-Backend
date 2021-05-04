package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.services.ProblemService;

import java.util.List;

@RestController
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @PostMapping()
    public ResponseEntity<Problem> createNewProblem(@MatrixVariable(value = "ownerId") int ownerId, @RequestBody Problem problem) {
        //TODO validate problem
        return requireNonNullOrElse(problemService.createNewProblem(ownerId, problem),
                ResponseEntity.badRequest().build());
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<Problem> getProblem(@PathVariable int problemId) {
        return requireNonNullOrElse(problemService.getProblem(problemId), ResponseEntity.notFound().build());
    }

    @GetMapping("/{problemId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissions(@PathVariable int problemId) {
        return requireNonNullOrElse(problemService.getAllSubmissions(problemId),
                ResponseEntity.notFound().build());
    }

    private <T> ResponseEntity<T> requireNonNullOrElse(T ifNotNull, ResponseEntity<T> ifNull) {
        if (ifNotNull == null) {
            return ifNull;
        } else {
            return ResponseEntity.ok(ifNotNull);
        }
    }
}
