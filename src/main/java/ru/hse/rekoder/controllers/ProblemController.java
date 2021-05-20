package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.requests.SubmissionRequest;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.responses.SubmissionResponse;
import ru.hse.rekoder.services.ProblemService;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponse> getProblem(@PathVariable int problemId) {
        Problem problem = problemService.getProblem(problemId);
        return ResponseEntity.ok(new ProblemResponse(problem));
    }

    @GetMapping("/{problemId}/submissions")
    public ResponseEntity<List<SubmissionResponse>> getSubmissions(@PathVariable int problemId) {
        return ResponseEntity.ok(problemService.getAllSubmissions(problemId)
                .stream()
                .map(SubmissionResponse::new)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{problemId}/submissions")
    public ResponseEntity<SubmissionResponse> createSubmission(@PathVariable int problemId,
                                                               @Valid @RequestBody SubmissionRequest submissionRequest) {
        Submission submission = new Submission();
        BeanUtils.copyProperties(submissionRequest, submission);
        Submission createdSubmission = problemService.createSubmission(problemId, submission);
        return ResponseEntity.ok(new SubmissionResponse(createdSubmission));
    }

    @PatchMapping(path = "/{problemId}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProblemResponse> updateProblem(@PathVariable int problemId,
                                                         @Valid @RequestBody JsonMergePatch jsonMergePatch) {
        Problem problem = problemService.updateProblem(problemId, jsonMergePatch);
        return ResponseEntity.ok(new ProblemResponse(problem));
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<?> deleteProblem(@PathVariable int problemId) {
        problemService.deleteProblem(problemId);
        return ResponseEntity.noContent().build();
    }
}
