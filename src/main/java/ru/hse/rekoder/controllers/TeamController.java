package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.rekoder.exceptions.AccessDeniedException;
import ru.hse.rekoder.exceptions.TeamException;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Team;
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
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;
    private final JsonMergePatchService jsonMergePatchService;

    public TeamController(TeamService teamService,
                          JsonMergePatchService jsonMergePatchService) {
        this.teamService = teamService;
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
        checkAccess(teamId, authentication.getName());
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
        checkAccess(teamId, authentication.getName());
        boolean userWasInTeam = teamService.deleteUserFromTeam(teamId, userId);
        if (!userWasInTeam) {
            throw new TeamException("There is not the user\"" + userId + "\" in the team");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}/problems")
    public ResponseEntity<List<ProblemResponse>> getProblems(@PathVariable String teamId) {
        return ResponseEntity.ok(teamService.getAllProblems(teamId)
                .stream()
                .map(ProblemResponse::new)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{teamId}/problems")
    public ResponseEntity<ProblemResponse> createProblem(@PathVariable String teamId,
                                                         @Valid @RequestBody ProblemRequest problemRequest,
                                                         Authentication authentication) {
        checkAccess(teamId, authentication.getName());
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemRequest, problem);
        Problem createdProblem = teamService.createProblem(teamId, problem);
        return ResponseEntity.ok(new ProblemResponse(createdProblem));
    }

    @PatchMapping(path = "/{teamId}", consumes = "application/merge-patch+json")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable String teamId,
                                                   @RequestBody JsonMergePatch jsonMergePatch,
                                                   Authentication authentication) {
        checkAccess(teamId, authentication.getName());
        Team team = teamService.getTeam(teamId);
        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(team), TeamRequest.class),
                team
        );
        Team updatedTeam = teamService.updateTeam(team);
        return ResponseEntity.ok(new TeamResponse(updatedTeam));
    }

    private void checkAccess(String teamName, String username) {
        Team team = teamService.getTeam(teamName);
        if (!team.getMemberIds().contains(username)) {
            throw new AccessDeniedException();
        }
    }

    private TeamRequest convertToRequest(Team team) {
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setBio(team.getBio());
        teamRequest.setName(team.getName());
        teamRequest.setContacts(team.getContacts());
        return teamRequest;
    }
}
