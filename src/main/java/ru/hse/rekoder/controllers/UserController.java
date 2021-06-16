package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.model.Password;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.requests.ProblemIdWrap;
import ru.hse.rekoder.requests.ProblemRequest;
import ru.hse.rekoder.requests.UserPatchRequest;
import ru.hse.rekoder.requests.UserRequest;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.responses.TeamResponse;
import ru.hse.rekoder.responses.UserResponse;
import ru.hse.rekoder.services.JsonMergePatchService;
import ru.hse.rekoder.services.TeamService;
import ru.hse.rekoder.services.UserService;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;
    private final TeamService teamService;
    private final AccessChecker accessChecker;
    private final JsonMergePatchService jsonMergePatchService;

    public UserController(UserService userService,
                          TeamService teamService,
                          AccessChecker accessChecker,
                          JsonMergePatchService jsonMergePatchService) {
        this.userService = userService;
        this.teamService = teamService;
        this.accessChecker = accessChecker;
        this.jsonMergePatchService = jsonMergePatchService;
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        Password password = new Password();
        password.setStringPassword(userRequest.getPassword());
        User user = new User();
        user.setUsername(userRequest.getId());
        user.setPassword(password);
        user.setBio(userRequest.getBio());
        user.setName(userRequest.getName());
        user.setContacts(userRequest.getContacts());
        User createdUser = userService.createUser(user);
        return ResponseEntity
                .created(URI.create("/users/" + createdUser.getUsername()))
                .body(new UserResponse(createdUser));
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
    public ResponseEntity<List<ProblemResponse>> getProblems(
            @RequestParam(required = false) Integer from,
            @Positive(message = "The size must be greater than 0") @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction,
            @PathVariable String userId) {
        return ResponseEntity.ok(userService.getProblems(
                userId, PageRequest.of(0, Integer.MAX_VALUE, direction, "id")
        )
                .stream()
                .dropWhile(problem -> Objects.nonNull(from) && !problem.getId().equals(from))
                .limit(Objects.requireNonNullElse(size, Integer.MAX_VALUE))
                .map(ProblemResponse::new)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{userId}/problems")
    public ResponseEntity<ProblemResponse> createProblem(@PathVariable String userId,
                                                         @Valid @RequestBody ProblemRequest problemRequest,
                                                         Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                Owner.userWithId(userId),
                Owner.userWithId(authentication.getName())
        );
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemRequest, problem);
        Problem createdProblem = userService.createProblem(userId, problem);
        return ResponseEntity
                .created(URI.create("/problems/" + createdProblem.getId()))
                .body(new ProblemResponse(createdProblem));
    }

    @PostMapping("/{userId}/problems/clone")
    public ResponseEntity<ProblemResponse> cloneProblem(@PathVariable String userId,
                                                        @Valid @RequestBody ProblemIdWrap originalProblem,
                                                        Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                Owner.userWithId(userId),
                Owner.userWithId(authentication.getName())
        );

        Problem problemClone = userService.cloneProblem(userId, originalProblem.getProblemId());
        return ResponseEntity
                .created(URI.create("/problems/" + problemClone.getId()))
                .body(new ProblemResponse(problemClone));
    }

    @PatchMapping(path = "/{userId}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId,
                                                   @RequestBody JsonMergePatch jsonMergePatch,
                                                   Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                Owner.userWithId(userId),
                Owner.userWithId(authentication.getName())
        );

        User user = userService.getUser(userId);
        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(user), UserPatchRequest.class),
                user);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(new UserResponse(updatedUser));
    }

    private UserPatchRequest convertToRequest(User user) {
        UserPatchRequest userPatchRequest = new UserPatchRequest();
        userPatchRequest.setBio(user.getBio());
        userPatchRequest.setName(user.getName());
        userPatchRequest.setContacts(user.getContacts());
        return userPatchRequest;
    }
}
