package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.responses.TeamResponse;
import ru.hse.rekoder.responses.UserResponse;
import ru.hse.rekoder.services.TeamService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable String teamId) {
        Team team = teamService.getTeam(teamId);
        return ResponseEntity.ok(new TeamResponse(team));
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid Team team) {
        Team createdTeam = teamService.createTeam(team);
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
    public ResponseEntity<TeamResponse> addMembers(@PathVariable String teamId,
                                                   @Valid @RequestBody @NotEmpty String member) {
        Team team = teamService.addExistingUsers(teamId, member);
        return ResponseEntity.ok(new TeamResponse(team));
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
                                                         @Valid @RequestBody Problem problem) {
        Problem createdProblem = teamService.createProblem(teamId, problem);
        return ResponseEntity.ok(new ProblemResponse(createdProblem));
    }
}
