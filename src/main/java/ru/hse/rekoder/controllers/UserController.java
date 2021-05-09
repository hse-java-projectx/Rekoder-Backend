package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.User;
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
    public ResponseEntity<User> getUser(@PathVariable int userId) {
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
    public ResponseEntity<List<Problem>> getProblems(@PathVariable int userId) {
        return ResponseEntity.ok(userService.getProblems(userId));
    }

    @PostMapping("/{userId}/problems")
    public ResponseEntity<Problem> createProblem(@PathVariable int userId,
                                                 @Valid @RequestBody Problem problem) {
        //TODO
        /*
        * @PostMapping()
    public ResponseEntity<Problem> createNewProblem(@MatrixVariable(value = "ownerId") int ownerId,
                                                    @Valid @RequestBody Problem problem) {
        return ResponseEntity.ok(problemService.createNewProblem(ownerId, problem));
    }
        *
        * */
        return null;
    }

    @PostMapping("/{userId}/problems")
    public ResponseEntity<Problem> createProblem(@PathVariable int userId, @RequestParam int origProblem) {
        //TODO
        return null;
    }

    @GetMapping("/{userId}/folders")
    public ResponseEntity<List<Folder>> getAllTopFolders(@PathVariable int userId) {
        //TODO
        return null;
    }

    @PostMapping("/{userId}/folders")
    public ResponseEntity<Folder> createFolder(@PathVariable int userId,
                                               @RequestBody @Valid Folder folder) {
        //TODO
        /*@PostMapping("/{folderId}/folders")
    public ResponseEntity<Folder> createFolder(@PathVariable int folderId,
                                               @RequestBody @Valid Folder folder) {
        return ResponseEntity.ok(folderService.createNewFolder(folderId, folder));
    }*/
        return null;
    }
}
