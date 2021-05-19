package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.Folder;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends MongoRepository<Folder, Integer> {
    Optional<Folder> findById(Integer id);
    List<Folder> findAllByParentFolderId(Integer id);
    boolean existsByParentFolderIdAndName(Integer parentFolderId, String name);
}
