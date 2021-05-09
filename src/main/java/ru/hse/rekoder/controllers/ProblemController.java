package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.services.ProblemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @PostMapping()
    public ResponseEntity<Problem> createNewProblem(@MatrixVariable(value = "ownerId") int ownerId,
                                                    @Valid @RequestBody Problem problem) {
        return ResponseEntity.ok(problemService.createNewProblem(ownerId, problem));
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<Problem> getProblem(@PathVariable int problemId) {
        return ResponseEntity.ok(problemService.getProblem(problemId));
    }

    @GetMapping("/{problemId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissions(@PathVariable int problemId) {
        return ResponseEntity.ok(problemService.getAllSubmissions(problemId));
    }
}