package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.responses.FolderResponse;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.services.FolderService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderResponse> getFolder(@PathVariable int folderId) {
        Folder folder = folderService.getFolder(folderId);
        return ResponseEntity.ok(new FolderResponse(folder));
    }

    @PostMapping("/{folderId}/folders")
    public ResponseEntity<FolderResponse> createFolder(@PathVariable int folderId,
                                                       @RequestBody @Valid Folder folder) {
        Folder createdFolder = folderService.createNewFolder(folderId, folder);
        return ResponseEntity.ok(new FolderResponse(createdFolder));
    }

    @GetMapping("/{folderId}/folders")
    public ResponseEntity<List<FolderResponse>> getSubFolders(@PathVariable int folderId) {
        return ResponseEntity.ok(folderService.getSubFolders(folderId)
                .stream()
                .map(FolderResponse::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{folderId}/problems")
    public ResponseEntity<List<ProblemResponse>> getProblems(@PathVariable int folderId) {
        return ResponseEntity.ok(folderService.getProblemsFromFolder(folderId)
                .stream()
                .map(ProblemResponse::new)
                .collect(Collectors.toList()));
    }

    @PatchMapping("/{folderId}/problems")
    public ResponseEntity<?> addProblemToFolder(@PathVariable int folderId, @RequestBody ProblemIdWrap problemToAdd) {
        folderService.addProblemToFolder(folderId, problemToAdd.getProblemId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{folderId}/problems")
    public ResponseEntity<?> deleteProblemFromFolder(@PathVariable int folderId, @RequestBody ProblemIdWrap problemToAdd) {
        folderService.deleteProblemFromFolder(folderId, problemToAdd.getProblemId());
        return ResponseEntity.noContent().build();
    }

    private static class ProblemIdWrap {
        private int problemId;

        public int getProblemId() {
            return problemId;
        }

        public void setProblemId(int problemId) {
            this.problemId = problemId;
        }
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable int folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }
}
