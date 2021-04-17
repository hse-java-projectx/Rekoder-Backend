package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.services.UserService;

import java.util.List;

/*
This class can be nested class in the UserController
*/

@RestController
@RequestMapping("/users/{userId}/problems")
public class ProblemUserController {
    //TODO validate methods arguments and handle incorrect query

    private final UserService userService;

    public ProblemUserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping()
    public ResponseEntity<Problem> createNewProblem(@PathVariable int userId, @RequestBody Problem problem) {
        //TODO validate problem
        return requireNonNullOrElse(userService.createNewProblem(userId, problem),
                ResponseEntity.badRequest().build());
    }

    @GetMapping()
    public ResponseEntity<List<Problem>> getProblems(@PathVariable int userId) {
        return requireNonNullOrElse(userService.getProblems(userId), ResponseEntity.notFound().build());
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<Problem> getProblem(@PathVariable int userId, @PathVariable int problemId) {
        return requireNonNullOrElse(userService.getProblem(userId, problemId), ResponseEntity.notFound().build());
    }

    @GetMapping("/{problemId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissions(@PathVariable int userId, @PathVariable int problemId) {
        return requireNonNullOrElse(userService.getAllSubmissions(userId, problemId),
                ResponseEntity.notFound().build());
    }

    @GetMapping("/{problemId}/submissions/{submissionId}")
    public ResponseEntity<Submission> getSubmission(@PathVariable int userId,
                                                    @PathVariable int problemId,
                                                    @PathVariable int submissionId) {
        return requireNonNullOrElse(userService.getSubmission(userId, problemId, submissionId),
                ResponseEntity.notFound().build());
    }

    @PostMapping("/{problemId}/submissions")
    public ResponseEntity<Submission> createSubmission(@PathVariable int userId,
                                                       @PathVariable int problemId,
                                                       @RequestBody Submission submission) {
        //TODO validate submission
        return requireNonNullOrElse(userService.addSubmission(userId, problemId, submission),
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
