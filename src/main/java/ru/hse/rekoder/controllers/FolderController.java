package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.responses.FolderResponse;
import ru.hse.rekoder.services.FolderService;

import javax.validation.Valid;

@RestController
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
}
