package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UpdatableRepositoryImpl<T, ID> implements UpdatableRepository<T, ID> {
    private final MongoOperations mongoOperations;

    public UpdatableRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Optional<T> update(T entity, ID id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return Optional.ofNullable(mongoOperations.findAndReplace(query,
                entity,
                FindAndReplaceOptions.options().returnNew(),
                mongoOperations.getCollectionName(entity.getClass())));
    }
}
