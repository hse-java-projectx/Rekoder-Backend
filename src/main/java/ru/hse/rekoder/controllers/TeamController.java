package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.services.TeamService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeam(@PathVariable int teamId) {
        //TODO
        return null;
    }

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody @Valid Team team) {
        //TODO
        return null;
    }

    @GetMapping("/{teamId}/users")
    public ResponseEntity<List<User>> getAllMembers(@PathVariable int teamId) {
        //TODO
        return null;
    }

    @PostMapping("/{teamId}/users")
    public ResponseEntity<User> addMember(@PathVariable int teamId/*TODO member*/) {
        //TODO
        return null;
    }

    @GetMapping("/{userId}/problems")
    public ResponseEntity<List<Problem>> getProblems(@PathVariable int userId) {
        //TODO
        return null;
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
