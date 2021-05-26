package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Team;

import java.util.Optional;

public class ModifiableFolderRepositoryImpl implements ModifiableFolderRepository {
    private final MongoOperations mongoOperations;

    public ModifiableFolderRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Optional<Boolean> addProblemToFolderById(int folderId, int problemId) {
        Query folderById = Query.query(Criteria.where("_id").is(folderId));
        Update update = new Update();
        update.addToSet("problemIds", problemId);
        Folder oldFolder = mongoOperations.findAndModify(folderById,
                update,
                FindAndModifyOptions.options().returnNew(false),
                Folder.class);
        return Optional.ofNullable(oldFolder).map((folder) -> !folder.getProblemIds().contains(problemId));
    }

    @Override
    public Optional<Boolean> deleteProblemToFolderById(int folderId, int problemId) {
        Query folderById = Query.query(Criteria.where("_id").is(folderId));
        Update update = new Update();
        update.pull("problemIds", problemId);
        Folder oldFolder = mongoOperations.findAndModify(folderById,
                update,
                FindAndModifyOptions.options().returnNew(false),
                Folder.class);
        return Optional.ofNullable(oldFolder).map((folder) -> folder.getProblemIds().contains(problemId));

    }
}
