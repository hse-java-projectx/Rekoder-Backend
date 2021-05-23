package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.exceptions.AccessDeniedException;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.requests.SubmissionRequest;
import ru.hse.rekoder.responses.SubmissionResponse;
import ru.hse.rekoder.services.JsonMergePatchService;
import ru.hse.rekoder.services.ProblemService;
import ru.hse.rekoder.services.SubmissionService;
import ru.hse.rekoder.services.TeamService;

import javax.json.JsonMergePatch;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;
    private final JsonMergePatchService jsonMergePatchService;
    private final ProblemService problemService;
    private final TeamService teamService;

    public SubmissionController(SubmissionService submissionService,
                                JsonMergePatchService jsonMergePatchService,
                                ProblemService problemService,
                                TeamService teamService) {
        this.submissionService = submissionService;
        this.jsonMergePatchService = jsonMergePatchService;
        this.problemService = problemService;
        this.teamService = teamService;
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
        checkAccess(submissionId, authentication.getName());
        Submission submission = submissionService.getSubmission(submissionId);
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
        checkAccess(submissionId, authentication.getName());
        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.noContent().build();
    }

    private void checkAccess(int submissionId, String username) {
        Submission submission = submissionService.getSubmission(submissionId);
        Problem problem = problemService.getProblem(submission.getProblemId());
        if (problem.getOwnerId().getProblemOwnerType().equals("user")
                && !problem.getOwnerId().getProblemOwnerId().equals(username)) {
            throw new AccessDeniedException();
        } else if (problem.getOwnerId().getProblemOwnerType().equals("team")) {
            if (!teamService.getTeam(problem.getOwnerId().getProblemOwnerId()).getMemberIds().contains(username)) {
                throw new AccessDeniedException();
            }
        }
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
