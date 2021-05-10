package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.services.TeamService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeam(@PathVariable String teamId) {
        return ResponseEntity.ok(teamService.getTeam(teamId));
    }

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody @Valid Team team) {
        return ResponseEntity.ok(teamService.createTeam(team));
    }

    @GetMapping("/{teamId}/users")
    public ResponseEntity<List<User>> getAllMembers(@PathVariable String teamId) {
        return ResponseEntity.ok(teamService.getAllMembers(teamId));
    }

    @PostMapping("/{teamId}/users")
    public ResponseEntity<Team> addMembers(@PathVariable String teamId,
                                           @Valid @RequestBody @NotEmpty Set<@NotEmpty String> members) {
        return ResponseEntity.ok(teamService.addExistingUsers(teamId, members));
    }

    @GetMapping("/{teamId}/problems")
    public ResponseEntity<List<Problem>> getProblems(@PathVariable String teamId) {
        return ResponseEntity.ok(teamService.getAllProblems(teamId));
    }

    @PostMapping("/{teamId}/problems")
    public ResponseEntity<Problem> createProblem(@PathVariable String teamId,
                                                 @Valid @RequestBody Problem problem) {
        return ResponseEntity.ok(teamService.createProblem(teamId, problem));
    }

    @GetMapping("/{teamId}/folders")
    public ResponseEntity<List<Folder>> getAllTopFolders(@PathVariable String teamId) {
        return ResponseEntity.ok(teamService.getTopFolders(teamId));
    }

    @PostMapping("/{teamId}/folders")
    public ResponseEntity<Folder> createFolder(@PathVariable String teamId,
                                               @RequestBody @Valid Folder folder) {
        return ResponseEntity.ok(teamService.createTopFolder(teamId, folder));
    }
}
