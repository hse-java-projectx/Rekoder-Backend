package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.exceptions.AccessDeniedException;
import ru.hse.rekoder.model.ContentGeneratorType;
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
@CrossOrigin(origins = "*")
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemService problemService;
    private final JsonMergePatchService jsonMergePatchService;
    private final TeamService teamService;

    public ProblemController(ProblemService problemService,
                             JsonMergePatchService jsonMergePatchService,
                             TeamService teamService) {
        this.problemService = problemService;
        this.jsonMergePatchService = jsonMergePatchService;
        this.teamService = teamService;
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
        checkAccess(problemId, authentication.getName());
        Submission submission = new Submission();
        BeanUtils.copyProperties(submissionRequest, submission);
        Submission createdSubmission = problemService.createSubmission(problemId, submission, authentication.getName());
        return ResponseEntity.ok(new SubmissionResponse(createdSubmission));
    }

    @PatchMapping(path = "/{problemId}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProblemResponse> updateProblem(@PathVariable int problemId,
                                                         @Valid @RequestBody JsonMergePatch jsonMergePatch,
                                                         Authentication authentication) {
        checkAccess(problemId, authentication.getName());
        Problem problem = problemService.getProblem(problemId);
        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(problem), ProblemRequest.class),
                problem);
        Problem updatedProblem = problemService.updateProblem(problem);
        return ResponseEntity.ok(new ProblemResponse(updatedProblem));
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<?> deleteProblem(@PathVariable int problemId,
                                           Authentication authentication) {
        checkAccess(problemId, authentication.getName());
        problemService.deleteProblem(problemId);
        return ResponseEntity.noContent().build();
    }

    private void checkAccess(int problemId, String username) {
        Problem problem = problemService.getProblem(problemId);
        if (problem.getOwner().getType().equals(ContentGeneratorType.USER)
                && !problem.getOwner().getId().equals(username)) {
            throw new AccessDeniedException();
        } else if (problem.getOwner().getType().equals(ContentGeneratorType.TEAM)) {
            if (!teamService.getTeam(problem.getOwner().getId()).getMemberIds().contains(username)) {
                throw new AccessDeniedException();
            }
        }
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
