package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.exceptions.AccessDeniedException;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.requests.ProblemRequest;
import ru.hse.rekoder.requests.SubmissionRequest;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.responses.SubmissionResponse;
import ru.hse.rekoder.services.JsonMergePatchService;
import ru.hse.rekoder.services.ProblemService;
import ru.hse.rekoder.services.TeamService;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemService problemService;
    private final JsonMergePatchService jsonMergePatchService;
    private final AccessChecker accessChecker;

    public ProblemController(ProblemService problemService,
                             JsonMergePatchService jsonMergePatchService,
                             TeamService teamService,
                             AccessChecker accessChecker) {
        this.problemService = problemService;
        this.jsonMergePatchService = jsonMergePatchService;
        this.accessChecker = accessChecker;
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
                                                               @Valid @RequestBody SubmissionRequest submissionRequest,
                                                               Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                problemService.getProblem(problemId).getOwner(),
                Owner.userWithId(authentication.getName())
        );
        Submission submission = new Submission();
        BeanUtils.copyProperties(submissionRequest, submission);
        Submission createdSubmission = problemService.createSubmission(problemId, submission, authentication.getName());
        return ResponseEntity.ok(new SubmissionResponse(createdSubmission));
    }

    @PatchMapping(path = "/{problemId}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProblemResponse> updateProblem(@PathVariable int problemId,
                                                         @Valid @RequestBody JsonMergePatch jsonMergePatch,
                                                         Authentication authentication) {
        Problem problem = problemService.getProblem(problemId);

        accessChecker.checkAccessToResourceWithOwner(
                problem.getOwner(),
                Owner.userWithId(authentication.getName())
        );

        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(problem), ProblemRequest.class),
                problem);
        Problem updatedProblem = problemService.updateProblem(problem);
        return ResponseEntity.ok(new ProblemResponse(updatedProblem));
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<?> deleteProblem(@PathVariable int problemId,
                                           Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                problemService.getProblem(problemId).getOwner(),
                Owner.userWithId(authentication.getName())
        );
        problemService.deleteProblem(problemId);
        return ResponseEntity.noContent().build();
    }

    private ProblemRequest convertToRequest(Problem problem) {
        ProblemRequest problemRequest = new ProblemRequest();
        problemRequest.setProblemUrl(problem.getProblemUrl());
        problemRequest.setName(problem.getName());
        problemRequest.setStatement(problem.getStatement());
        problemRequest.setTags(problem.getTags());
        problemRequest.setTests(problem.getTests());
        return problemRequest;
    }
}
