package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.hse.rekoder.model.Folder;

import javax.swing.text.StyledEditorKit;
import java.util.List;
import java.util.Optional;

public interface FolderRepository extends MongoRepository<Folder, Integer>, FolderGraphLookupRepository {
    Optional<Folder> findById(Integer id);
    List<Folder> findAllByParentFolderId(Integer id);
    boolean existsByParentFolderIdAndName(Integer parentFolderId, String name);
    boolean existsByParentFolderIdAndNameAndIdIsNot(Integer parentFolderId, String name, Integer id);
    void deleteByIdIn(List<Integer> ids);
}
