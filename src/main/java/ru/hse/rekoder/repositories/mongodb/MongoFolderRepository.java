package ru.hse.rekoder.repositories.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.mongodb.seqGenerators.DatabaseIntSequenceService;

import java.util.Optional;

//@Repository
public class MongoFolderRepository /*implements FolderRepository*/ {
    private static final String COLLECTION_NAME = "folders";
    private static final String SEQUENCE_NAME = "folder_sequence";

    private final MongoOperations mongoOperations;
    private final DatabaseIntSequenceService sequenceGeneratorService;

    public MongoFolderRepository(MongoOperations mongoOperations,
                                 DatabaseIntSequenceService sequenceGeneratorService) {
        this.mongoOperations = mongoOperations;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    //@Override
    public Optional<Folder> findById(Integer id) {
        return Optional.ofNullable(mongoOperations.findById(id, Folder.class, COLLECTION_NAME));
    }

    //@Override
    public Folder save(Folder folder) {
        if (folder.getId() == null) {
            folder.setId(sequenceGeneratorService.generateSequence(SEQUENCE_NAME));
        }
        return mongoOperations.save(folder, COLLECTION_NAME);
    }
}
