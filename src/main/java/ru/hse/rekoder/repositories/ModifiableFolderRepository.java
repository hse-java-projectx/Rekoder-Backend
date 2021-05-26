package ru.hse.rekoder.repositories;

import java.util.Optional;

public interface ModifiableFolderRepository {
    Optional<Boolean> addProblemToFolderById(int folderId, int problemId);
    Optional<Boolean> deleteProblemToFolderById(int folderId, int problemId);
}
