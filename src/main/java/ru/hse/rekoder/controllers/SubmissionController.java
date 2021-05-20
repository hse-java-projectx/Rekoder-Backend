package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.requests.SubmissionRequest;
import ru.hse.rekoder.responses.SubmissionResponse;
import ru.hse.rekoder.services.JsonMergePatchService;
import ru.hse.rekoder.services.SubmissionService;

import javax.json.JsonMergePatch;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;
    private final JsonMergePatchService jsonMergePatchService;

    public SubmissionController(SubmissionService submissionService,
                                JsonMergePatchService jsonMergePatchService) {
        this.submissionService = submissionService;
        this.jsonMergePatchService = jsonMergePatchService;
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResponse> getSubmission(@PathVariable int submissionId) {
        Submission submission = submissionService.getSubmission(submissionId);
        return ResponseEntity.ok(new SubmissionResponse(submission));
    }

    @PatchMapping(path = "/{submissionId}", consumes = "application/merge-patch+json")
    public ResponseEntity<SubmissionResponse> updateSubmission(@PathVariable int submissionId,
                                                               @RequestBody JsonMergePatch jsonMergePatch) {
        Submission submission = submissionService.getSubmission(submissionId);
        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(submission), SubmissionRequest.class),
                submission
        );
        Submission updatedSubmission = submissionService.updateSubmission(submission);
        return ResponseEntity.ok(new SubmissionResponse(updatedSubmission));
    }

    @DeleteMapping("/{submissionId}")
    public ResponseEntity<?> deleteSubmission(@PathVariable int submissionId) {
        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.noContent().build();
    }

    private SubmissionRequest convertToRequest(Submission submission) {
        SubmissionRequest submissionRequest = new SubmissionRequest();
        submissionRequest.setComment(submission.getComment());
        submissionRequest.setCompiler(submission.getCompiler());
        submissionRequest.setFeedback(submission.getFeedback());
        submissionRequest.setSourceCode(submission.getSourceCode());
        return submissionRequest;
    }
}
