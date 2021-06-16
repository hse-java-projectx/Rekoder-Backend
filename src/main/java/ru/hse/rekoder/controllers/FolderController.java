package ru.hse.rekoder.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.exceptions.FolderException;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.requests.FolderRequest;
import ru.hse.rekoder.requests.ProblemIdWrap;
import ru.hse.rekoder.responses.FolderResponse;
import ru.hse.rekoder.responses.ProblemResponse;
import ru.hse.rekoder.services.FolderService;
import ru.hse.rekoder.services.JsonMergePatchService;
import ru.hse.rekoder.services.ProblemService;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;
    private final ProblemService problemService;
    private final JsonMergePatchService jsonMergePatchService;
    private final AccessChecker accessChecker;

    public FolderController(FolderService folderService,
                            ProblemService problemService,
                            JsonMergePatchService jsonMergePatchService,
                            AccessChecker accessChecker) {
        this.folderService = folderService;
        this.problemService = problemService;
        this.jsonMergePatchService = jsonMergePatchService;
        this.accessChecker = accessChecker;
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderResponse> getFolder(@PathVariable int folderId) {
        Folder folder = folderService.getFolder(folderId);
        return ResponseEntity.ok(new FolderResponse(folder));
    }

    @PostMapping("/{folderId}/folders")
    public ResponseEntity<FolderResponse> createFolder(@PathVariable int folderId,
                                                       @RequestBody @Valid FolderRequest folderRequest,
                                                       Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                folderService.getFolder(folderId).getOwner(),
                Owner.userWithId(authentication.getName())
        );

        Folder folder = new Folder();
        BeanUtils.copyProperties(folderRequest, folder);
        Folder createdFolder = folderService.createNewFolder(folderId, folder);
        return ResponseEntity
                .created(URI.create("/folders/" + createdFolder.getId()))
                .body(new FolderResponse(createdFolder));
    }

    @GetMapping("/{folderId}/path-to-root")
    public ResponseEntity<List<FolderResponse>> getPathToRootFrom(@PathVariable int folderId) {
        return ResponseEntity.ok(folderService.getPathToRootFrom(folderId)
                .stream()
                .map(FolderResponse::new)
                .collect(Collectors.toList()));
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
    public ResponseEntity<?> addProblemToFolder(@PathVariable int folderId, @RequestBody ProblemIdWrap problemToAdd,
                                                Authentication authentication) {
        Folder folder = folderService.getFolder(folderId);
        accessChecker.checkAccessToResourceWithOwner(
                folder.getOwner(),
                Owner.userWithId(authentication.getName())
        );
        accessChecker.checkAccessToResourceWithOwner(
                problemService.getProblem(problemToAdd.getProblemId()).getOwner(),
                folder.getOwner()
        );
        boolean isNewProblemForFolder = folderService.addProblemToFolder(folderId, problemToAdd.getProblemId());
        if (!isNewProblemForFolder) {
            throw new FolderException("The problem " + problemToAdd.getProblemId() + " is already in the folder " + folderId);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{folderId}/problems/{problemId}")
    public ResponseEntity<?> deleteProblemFromFolder(@PathVariable int folderId, @PathVariable int problemId,
                                                     Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                folderService.getFolder(folderId).getOwner(),
                Owner.userWithId(authentication.getName())
        );
        boolean problemWasInFolder = folderService.deleteProblemFromFolder(folderId, problemId);
        if (!problemWasInFolder) {
            throw new FolderException("There is not the problem " + problemId + " in the folder " + folderId);
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{folderId}", consumes = "application/merge-patch+json")
    public ResponseEntity<FolderResponse> updateProblem(@PathVariable int folderId,
                                                        @Valid @RequestBody JsonMergePatch jsonMergePatch,
                                                        Authentication authentication) {
        Folder folder = folderService.getFolder(folderId);
        accessChecker.checkAccessToResourceWithOwner(
                folder.getOwner(),
                Owner.userWithId(authentication.getName())
        );
        BeanUtils.copyProperties(
                jsonMergePatchService.mergePatch(jsonMergePatch, convertToRequest(folder), FolderRequest.class),
                folder);
        Folder updatedFolder = folderService.updateFolder(folder);
        return ResponseEntity.ok(new FolderResponse(updatedFolder));
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable int folderId,
                                          Authentication authentication) {
        accessChecker.checkAccessToResourceWithOwner(
                folderService.getFolder(folderId).getOwner(),
                Owner.userWithId(authentication.getName())
        );
        if (Objects.isNull(folderService.getFolder(folderId).getParentFolderId())) {
            throw new FolderException("The root folder cannot be deleted");
        }
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }

    private FolderRequest convertToRequest(Folder folder) {
        FolderRequest folderRequest = new FolderRequest();
        folderRequest.setName(folder.getName());
        return folderRequest;
    }
}
