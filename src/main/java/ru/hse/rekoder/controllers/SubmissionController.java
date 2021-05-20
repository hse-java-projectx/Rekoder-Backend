package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.responses.SubmissionResponse;
import ru.hse.rekoder.services.SubmissionService;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResponse> getSubmission(@PathVariable int submissionId) {
        Submission submission = submissionService.getSubmission(submissionId);
        return ResponseEntity.ok(new SubmissionResponse(submission));
    }

    @DeleteMapping("/{submissionId}")
    public ResponseEntity<?> deleteSubmission(@PathVariable int submissionId) {
        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.noContent().build();
    }
}
