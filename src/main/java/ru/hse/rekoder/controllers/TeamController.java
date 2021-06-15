package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.exceptions.TeamException;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.requests.ProblemIdWrap;
import ru.hse.rekoder.requests.ProblemRequest;
import ru.hse.rekoder.requests.TeamMemberWrap;
import ru.hse.rekoder.requests.TeamRequest;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.responses.TeamResponse;
import ru.hse.rekoder.responses.UserResponse;
import ru.hse.rekoder.services.JsonMergePatchService;
import ru.hse.rekoder.services.TeamService;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
@Validated
public class TeamController {
    private final TeamService teamService;
    private final AccessChecker accessChecker;
    private final JsonMergePatchService jsonMergePatchService;

    public TeamController(TeamService teamService,
                          AccessChecker accessChecker,
                          JsonMergePatchService jsonMergePatchService) {
        this.teamService = teamService;
        this.accessChecker = accessChecker;
        this.jsonMergePatchService = jsonMergePatchService;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable String teamId) {
        Team team = teamService.getTeam(teamId);
        return ResponseEntity.ok(new TeamResponse(team));
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid Team team,
                                                   Authentication authentication) {
        Team createdTeam = teamService.createTeam(team, authentication.getName());
        return ResponseEntity.ok(new TeamResponse(createdTeam));
    }

    @GetMapping("/{teamId}/users")
    public ResponseEntity<List<UserResponse>> getAllMembers(@PathVariable String teamId) {
        return ResponseEntity.ok(teamService.getAllMembers(teamId)
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{teamId}/users")
    public ResponseEntity<?> addMember(@PathVariable String teamId,
                                       @Valid @RequestBody TeamMemberWrap member,
                                       Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                Owner.teamWithId(teamId),
                Owner.userWithId(authentication.getName())
        );
        boolean isNewMember = teamService.addExistingUserToTeam(teamId, member.getMemberId());
        if (!isNewMember) {
            throw new TeamException("User \"" + member.getMemberId() + "\" already in the team");
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teamId}/users/{userId}")
    public ResponseEntity<?> deleteMember(@PathVariable String teamId,
                                          @PathVariable String userId,
                                          Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                Owner.teamWithId(teamId),
                Owner.userWithId(authentication.getName())
        );
        boolean userWasInTeam = teamService.deleteUserFromTeam(teamId, userId);
        if (!userWasInTeam) {
            throw new TeamException("There is not the user\"" + userId + "\" in the team");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}/problems")
    public ResponseEntity<List<ProblemResponse>> getProblems(
            @RequestParam(required = false) Integer from,
            @Positive(message = "The size must be greater than 0") @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction,
            @PathVariable String teamId) {
        return ResponseEntity.ok(teamService.getAllProblems(
                teamId,
                PageRequest.of(0, Integer.MAX_VALUE, direction, "id")
        )
                .stream()
                .dropWhile(problem -> Objects.nonNull(from) && !problem.getId().equals(from))
                .limit(Objects.requireNonNullElse(size, Integer.MAX_VALUE))
                .map(ProblemResponse::new)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{teamId}/problems")
    public ResponseEntity<ProblemResponse> createProblem(@PathVariable String teamId,
                                                         @Valid @RequestBody ProblemRequest problemRequest,
                                                         Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                Owner.teamWithId(teamId),
                Owner.userWithId(authentication.getName())
        );
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemRequest, problem);
        Problem createdProblem = teamService.createProblem(teamId, problem);
        return ResponseEntity.ok(new ProblemResponse(createdProblem));
    }

    @PostMapping("/{teamId}/problems/clone")
    public ResponseEntity<ProblemResponse> cloneProblem(@PathVariable String teamId,
                                                        @Valid @RequestBody ProblemIdWrap originalProblem,
                                                        Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                Owner.teamWithId(teamId),
                Owner.userWithId(authentication.getName())
        );
        Problem problemClone = teamService.cloneProblem(teamId, originalProblem.getProblemId());
        return ResponseEntity.ok(new ProblemResponse(problemClone));
    }

    @PatchMapping(path = "/{teamId}", consumes = "application/merge-patch+json")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable String teamId,
                                                   @RequestBody JsonMergePatch jsonMergePatch,
                                                   Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                Owner.teamWithId(teamId),
                Owner.userWithId(authentication.getName())
        );
        Team team = teamService.getTeam(teamId);
        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(team), TeamRequest.class),
                team
        );
        Team updatedTeam = teamService.updateTeam(team);
        return ResponseEntity.ok(new TeamResponse(updatedTeam));
    }

    private TeamRequest convertToRequest(Team team) {
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setBio(team.getBio());
        teamRequest.setName(team.getName());
        teamRequest.setContacts(team.getContacts());
        return teamRequest;
    }
}
