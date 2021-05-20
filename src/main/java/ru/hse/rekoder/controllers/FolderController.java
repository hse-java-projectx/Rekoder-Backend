package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.requests.FolderRequest;
import ru.hse.rekoder.requests.ProblemIdWrap;
import ru.hse.rekoder.responses.FolderResponse;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.services.FolderService;
import ru.hse.rekoder.services.JsonMergePatchService;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;
    private final JsonMergePatchService jsonMergePatchService;

    public FolderController(FolderService folderService,
                            JsonMergePatchService jsonMergePatchService) {
        this.folderService = folderService;
        this.jsonMergePatchService = jsonMergePatchService;
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderResponse> getFolder(@PathVariable int folderId) {
        Folder folder = folderService.getFolder(folderId);
        return ResponseEntity.ok(new FolderResponse(folder));
    }

    @PostMapping("/{folderId}/folders")
    public ResponseEntity<FolderResponse> createFolder(@PathVariable int folderId,
                                                       @RequestBody @Valid FolderRequest folderRequest) {
        Folder folder = new Folder();
        BeanUtils.copyProperties(folderRequest, folder);
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

    @DeleteMapping("/{folderId}/problems/{problemId}")
    public ResponseEntity<?> deleteProblemFromFolder(@PathVariable int folderId, @PathVariable int problemId) {
        folderService.deleteProblemFromFolder(folderId, problemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{folderId}", consumes = "application/merge-patch+json")
    public ResponseEntity<FolderResponse> updateProblem(@PathVariable int folderId,
                                                         @Valid @RequestBody JsonMergePatch jsonMergePatch) {
        Folder folder = folderService.getFolder(folderId);
        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(folder), FolderRequest.class),
                folder);
        Folder updatedFolder = folderService.updateFolder(folder);
        return ResponseEntity.ok(new FolderResponse(updatedFolder));
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable int folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }

    private FolderRequest convertToRequest(Folder folder) {
        FolderRequest folderRequest = new FolderRequest();
        folderRequest.setName(folder.getName());
        return folderRequest;
    }
}
