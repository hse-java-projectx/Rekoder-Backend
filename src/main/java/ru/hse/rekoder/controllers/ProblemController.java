package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.requests.ProblemRequest;
import ru.hse.rekoder.requests.SubmissionRequest;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.responses.SubmissionResponse;
import ru.hse.rekoder.services.JsonMergePatchService;
import ru.hse.rekoder.services.ProblemService;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/problems")
@Validated
public class ProblemController {
    private final ProblemService problemService;
    private final JsonMergePatchService jsonMergePatchService;
    private final AccessChecker accessChecker;

    public ProblemController(ProblemService problemService,
                             JsonMergePatchService jsonMergePatchService,
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
    public ResponseEntity<List<SubmissionResponse>> getSubmissions(
            @RequestParam(required = false) Integer from,
            @Positive(message = "The size must be greater than 0") @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction,
            @PathVariable int problemId) {
        return ResponseEntity.ok(problemService.getAllSubmissions(
                problemId,
                PageRequest.of(0, Integer.MAX_VALUE, direction, "id")
        )
                .stream()
                .dropWhile(submission -> Objects.nonNull(from) && !submission.getId().equals(from))
                .limit(Objects.requireNonNullElse(size, Integer.MAX_VALUE))
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
        return ResponseEntity
                .created(URI.create("/submissions/" + createdSubmission.getId()))
                .body(new SubmissionResponse(createdSubmission));
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
