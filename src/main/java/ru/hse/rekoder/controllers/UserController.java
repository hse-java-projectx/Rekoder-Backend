package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.requests.ProblemRequest;
import ru.hse.rekoder.requests.UserRequest;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.responses.TeamResponse;
import ru.hse.rekoder.responses.UserResponse;
import ru.hse.rekoder.services.JsonMergePatchService;
import ru.hse.rekoder.services.TeamService;
import ru.hse.rekoder.services.UserService;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TeamService teamService;
    private final JsonMergePatchService jsonMergePatchService;

    public UserController(UserService userService,
                          TeamService teamService,
                          JsonMergePatchService jsonMergePatchService) {
        this.userService = userService;
        this.teamService = teamService;
        this.jsonMergePatchService = jsonMergePatchService;
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

    @GetMapping("/{userId}/teams")
    @ResponseBody
    public ResponseEntity<List<TeamResponse>> getTeamsUserIn(@PathVariable String userId) {
        return ResponseEntity.ok(teamService.getTeamsUserIn(userId)
                .stream()
                .map(TeamResponse::new)
                .collect(Collectors.toList()));
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
                                                         @Valid @RequestBody ProblemRequest problemRequest,
                                                         Authentication authentication) {
        if (!authentication.getName().equals(userId)) {
            System.out.println(authentication.getPrincipal());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemRequest, problem);
        Problem createdProblem = userService.createProblem(userId, problem);
        return ResponseEntity.ok(new ProblemResponse(createdProblem));
    }

    @PatchMapping(path = "/{userId}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId,
                                                   @RequestBody JsonMergePatch jsonMergePatch,
                                                   Authentication authentication) {
        if (!authentication.getName().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userService.getUser(userId);
        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(user), UserRequest.class),
                user);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(new UserResponse(updatedUser));
    }

    private UserRequest convertToRequest(User user) {
        UserRequest userRequest = new UserRequest();
        userRequest.setBio(user.getBio());
        userRequest.setName(user.getName());
        userRequest.setContacts(user.getContacts());
        return userRequest;
    }
}
