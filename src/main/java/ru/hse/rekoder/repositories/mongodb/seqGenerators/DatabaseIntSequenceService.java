package ru.hse.rekoder.repositories.mongodb.seqGenerators;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class DatabaseIntSequenceService {
    private final MongoOperations mongoOperations;

    public DatabaseIntSequenceService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public int generateSequence(String sequenceName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(sequenceName));
        Update update = new Update();
        update.inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);
        DatabaseIntSequence sequence = mongoOperations.findAndModify(query, update, options,
                DatabaseIntSequence.class, "database_sequence");
        Objects.requireNonNull(sequence, "Failed to generate sequence");
        return sequence.getSeq();
    }
}
