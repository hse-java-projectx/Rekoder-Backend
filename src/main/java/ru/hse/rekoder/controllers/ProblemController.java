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

    @GetMapping("/{problemId}")
    public ResponseEntity<Problem> getProblem(@PathVariable int problemId) {
        return ResponseEntity.ok(problemService.getProblem(problemId));
    }

    @GetMapping("/{problemId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissions(@PathVariable int problemId) {
        return ResponseEntity.ok(problemService.getAllSubmissions(problemId));
    }

    @PostMapping("/{problemId}/submissions")
    public ResponseEntity<Submission> createSubmission(@PathVariable int problemId,
                                                       @Valid @RequestBody Submission submission) {
        //TODO
        /*@PostMapping("/submissions")
    public ResponseEntity<Submission> createSubmission(@MatrixVariable(value = "problemId") int problemId,
                                                       @Valid @RequestBody Submission submission) {
        return ResponseEntity.ok(submissionService.createSubmission(problemId, submission));
    }*/
        return null;
    }
}
