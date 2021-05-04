package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.services.SubmissionService;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<Submission> getSubmission(@PathVariable int submissionId) {
        return requireNonNullOrElse(submissionService.getSubmission(submissionId),
                ResponseEntity.notFound().build());
    }

    @PostMapping("/submissions")
    public ResponseEntity<Submission> createSubmission(@MatrixVariable(value = "problemId") int problemId,
                                                       @RequestBody Submission submission) {
        //TODO validate submission
        return requireNonNullOrElse(submissionService.addSubmission(problemId, submission),
                ResponseEntity.badRequest().build());
    }

    private <T> ResponseEntity<T> requireNonNullOrElse(T ifNotNull, ResponseEntity<T> ifNull) {
        if (ifNotNull == null) {
            return ifNull;
        } else {
            return ResponseEntity.ok(ifNotNull);
        }
    }
}
