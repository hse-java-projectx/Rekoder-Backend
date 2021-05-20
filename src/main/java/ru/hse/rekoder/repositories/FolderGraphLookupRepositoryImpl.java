package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FolderGraphLookupRepositoryImpl implements FolderGraphLookupRepository {
    private final MongoTemplate mongoTemplate;

    public FolderGraphLookupRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Integer> getSubTree(int folderId) {
        final Criteria byFolderId = new Criteria("_id").is(folderId);
        final MatchOperation matchStage = Aggregation.match(byFolderId);
        //TODO restrict maxDepth
        GraphLookupOperation graphLookupOperation = GraphLookupOperation.builder()
                .from("folders")
                .startWith("$_id")
                .connectFrom("_id")
                .connectTo("parentFolderId")
                .as("subTree");

        ProjectionOperation projectStage = Aggregation.project(Fields.from(Fields.field("res", "$subTree._id")));

        Aggregation aggregation = Aggregation.newAggregation(matchStage, graphLookupOperation, projectStage);

        return mongoTemplate.aggregate(aggregation, "folders", SubTreeHolder.class).getMappedResults()
                .stream().map(SubTreeHolder::getRes).findFirst().orElseThrow(() -> new RuntimeException("Failed to find subTree"));
    }
}
