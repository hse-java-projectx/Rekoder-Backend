package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.FolderNotFoundException;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.repositories.FolderRepository;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
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
        folder.setId(null);
        folder.setOwner(parentFolder.getOwner());
        folder = folderRepository.save(folder);
        parentFolder.getSubfolders().add(folder);
        folderRepository.save(parentFolder);
        return folder;
    }

    @Override
    public List<Folder> getSubFolders(int folderId) {
        return folderRepository.findById(folderId)
                .map(Folder::getSubfolders)
                .orElseThrow(() -> new FolderNotFoundException("Folder with id \"" + folderId + "\" not found"));
    }

    @Override
    public List<Problem> getProblemsFromFolder(int folderId) {
        return folderRepository.findById(folderId)
                .map(Folder::getProblems)
                .orElseThrow(() -> new FolderNotFoundException("Folder with id \"" + folderId + "\" not found"));
    }
}
