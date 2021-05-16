package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;

import java.util.List;

public interface FolderService {
    Folder getFolder(int folderId);
    Folder createNewFolder(int parentFolderId, Folder folder);

    List<Folder> getSubFolders(int folderId);
    List<Problem> getProblemsFromFolder(int folderId);
}
