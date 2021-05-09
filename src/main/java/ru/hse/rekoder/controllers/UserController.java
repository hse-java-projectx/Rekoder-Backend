package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.UserRepository;
import ru.hse.rekoder.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(createdUser);
        }
    }

    @GetMapping("/{userId}/problems")
    public ResponseEntity<List<Problem>> getProblems(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getProblems(userId));
    }

    @PostMapping("/{userId}/problems")
    public ResponseEntity<Problem> createProblem(@PathVariable String userId,
                                                 @Valid @RequestBody Problem problem) {
        return ResponseEntity.ok(userService.createProblem(userId, problem));
    }

    @GetMapping("/{userId}/folders")
    public ResponseEntity<List<Folder>> getAllTopFolders(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getTopFolder(userId));
    }

    @PostMapping("/{userId}/folders")
    public ResponseEntity<Folder> createFolder(@PathVariable String userId,
                                               @RequestBody @Valid Folder folder) {
        return ResponseEntity.ok(userService.createTopFolder(userId, folder));
    }
}
