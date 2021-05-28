package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.requests.SubmissionRequest;
import ru.hse.rekoder.responses.SubmissionResponse;
import ru.hse.rekoder.services.JsonMergePatchService;
import ru.hse.rekoder.services.ProblemService;
import ru.hse.rekoder.services.SubmissionService;

import javax.json.JsonMergePatch;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;
    private final JsonMergePatchService jsonMergePatchService;
    private final ProblemService problemService;
    private final AccessChecker accessChecker;

    public SubmissionController(SubmissionService submissionService,
                                JsonMergePatchService jsonMergePatchService,
                                ProblemService problemService,
                                AccessChecker accessChecker) {
        this.submissionService = submissionService;
        this.jsonMergePatchService = jsonMergePatchService;
        this.problemService = problemService;
        this.accessChecker = accessChecker;
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResponse> getSubmission(@PathVariable int submissionId) {
        Submission submission = submissionService.getSubmission(submissionId);
        return ResponseEntity.ok(new SubmissionResponse(submission));
    }

    @PatchMapping(path = "/{submissionId}", consumes = "application/merge-patch+json")
    public ResponseEntity<SubmissionResponse> updateSubmission(@PathVariable int submissionId,
                                                               @RequestBody JsonMergePatch jsonMergePatch,
                                                               Authentication authentication) {
        Submission submission = submissionService.getSubmission(submissionId);
        accessChecker.checkAccessToResourceWithOwner(
                problemService.getProblem(submission.getProblemId()).getOwner(),
                Owner.userWithId(authentication.getName())
        );
        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(submission), SubmissionRequest.class),
                submission
        );
        Submission updatedSubmission = submissionService.updateSubmission(submission);
        return ResponseEntity.ok(new SubmissionResponse(updatedSubmission));
    }

    @DeleteMapping("/{submissionId}")
    public ResponseEntity<?> deleteSubmission(@PathVariable int submissionId,
                                              Authentication authentication) {
        Submission submission = submissionService.getSubmission(submissionId);
        accessChecker.checkAccessToResourceWithOwner(
                problemService.getProblem(submission.getProblemId()).getOwner(),
                Owner.userWithId(authentication.getName())
        );
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
