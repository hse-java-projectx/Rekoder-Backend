package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.UserRepository;
import ru.hse.rekoder.responses.FolderResponse;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.responses.UserResponse;
import ru.hse.rekoder.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(new UserResponse(createdUser));
        }
    }

    @GetMapping("/{userId}/problems")
    public ResponseEntity<List<ProblemResponse>> getProblems(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getProblems(userId)
                .stream()
                .map(ProblemResponse::new)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{userId}/problems")
    public ResponseEntity<ProblemResponse> createProblem(@PathVariable String userId,
                                                 @Valid @RequestBody Problem problem) {
        Problem createdProblem = userService.createProblem(userId, problem);
        return ResponseEntity.ok(new ProblemResponse(createdProblem));
    }
}
