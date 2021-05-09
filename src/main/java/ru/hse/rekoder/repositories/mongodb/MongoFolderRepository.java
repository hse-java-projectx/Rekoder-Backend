package ru.hse.rekoder.repositories.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.repositories.FolderRepository;

import java.util.Optional;

@Repository
public class MongoFolderRepository implements FolderRepository {
    private final MongoOperations mongoOperations;
    private int id = 0;

    public MongoFolderRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Optional<Folder> findById(Integer id) {
        return Optional.ofNullable(mongoOperations.findById(id, Folder.class, "folder"));
    }

    @Override
    public Folder save(Folder folder) {
        if (folder.getId() == null) {
            folder.setId(id);
            ++id;
        }
        return mongoOperations.save(folder, "folder");
    }
}
