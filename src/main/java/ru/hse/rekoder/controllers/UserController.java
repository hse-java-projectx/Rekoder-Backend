package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.services.UserService;

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
        User user = userService.getUser(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        //TODO validate user
        User createdUser = userService.addUser(user);
        if (createdUser == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(createdUser);
        }
    }

    @GetMapping("/{userId}/problems")
    public ResponseEntity<List<Problem>> getProblems(@PathVariable int userId) {
        return requireNonNullOrElse(userService.getProblems(userId), ResponseEntity.notFound().build());
    }

    private <T> ResponseEntity<T> requireNonNullOrElse(T ifNotNull, ResponseEntity<T> ifNull) {
        if (ifNotNull == null) {
            return ifNull;
        } else {
            return ResponseEntity.ok(ifNotNull);
        }
    }
}
