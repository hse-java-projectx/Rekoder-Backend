package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;

import java.util.List;

public interface FolderService {
    Folder getFolder(int folderId);
    Folder createNewFolder(int parentFolderId, Folder folder);
    Folder updateFolder(Folder folder);

    List<Folder> getPathToRootFrom(int folderId);
    List<Folder> getSubFolders(int folderId);
    List<Problem> getProblemsFromFolder(int folderId);

    boolean addProblemToFolder(int folderId, int problemId);
    boolean deleteProblemFromFolder(int folderId, int problemId);

    void deleteFolder(int folderId);
}
