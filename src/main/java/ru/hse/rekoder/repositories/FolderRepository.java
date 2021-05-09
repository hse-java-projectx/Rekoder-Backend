package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.Folder;

import java.util.Optional;

public interface FolderRepository {
    Optional<Folder> findById(Integer id);
    Folder save(Folder folder);
}
