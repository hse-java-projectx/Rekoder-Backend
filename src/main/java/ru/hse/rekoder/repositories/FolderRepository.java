package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.Folder;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends MongoRepository<Folder, Integer>,
        ModifiableFolderRepository, UpdatableRepository<Folder, Integer> {
    Optional<Folder> findById(Integer id);
    List<Folder> findAllByParentFolderId(Integer id);
    List<Folder> findAllByParentFolderIdIn(List<Integer> ids);
    boolean existsByParentFolderIdAndName(Integer parentFolderId, String name);
    boolean existsByParentFolderIdAndNameAndIdIsNot(Integer parentFolderId, String name, Integer id);
    void deleteByIdIn(List<Integer> ids);
}
