package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.Folder;

import java.util.Optional;

public interface ModifiableFolderRepository {
    Optional<Folder> addProblemToFolderById(int folderId, int problemId);
    Optional<Folder> deleteProblemFromFolderById(int folderId, int problemId);
}
