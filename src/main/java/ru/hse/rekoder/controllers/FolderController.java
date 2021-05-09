package ru.hse.rekoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.rekoder.model.Folder;
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
    public ResponseEntity<Folder> getFolder(@PathVariable int folderId) {
        return ResponseEntity.ok(folderService.getFolder(folderId));
    }

    @PostMapping("/{folderId}/folders")
    public ResponseEntity<Folder> createFolder(@PathVariable int folderId,
                                               @RequestBody @Valid Folder folder) {
        //TODO
        return ResponseEntity.ok(folderService.createNewFolder(folderId, folder));
    }
}
