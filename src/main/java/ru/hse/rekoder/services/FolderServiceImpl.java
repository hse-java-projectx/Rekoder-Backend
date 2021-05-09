package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.repositories.FolderRepository;

@Service
public class FolderServiceImpl implements FolderService{
    private final FolderRepository folderRepository;

    public FolderServiceImpl(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Override
    public Folder getFolder(int folderId) {
        return folderRepository.findById(folderId).orElseThrow();
    }

    @Override
    public Folder createNewFolder(int parentFolderId, Folder folder) {
        Folder parentFolder = folderRepository.findById(parentFolderId).orElseThrow();
        folder.setParentFolder(parentFolder);
        folder = folderRepository.save(folder);
        parentFolder.getSubfolders().add(folder);
        return folder;
    }
}
