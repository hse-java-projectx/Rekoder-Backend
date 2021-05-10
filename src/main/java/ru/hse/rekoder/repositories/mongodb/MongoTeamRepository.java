package ru.hse.rekoder.repositories.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.repositories.TeamRepository;

import java.util.Optional;

@Repository
public class MongoTeamRepository implements TeamRepository {
    private static final String COLLECTION_NAME = "problem_owners";

    private final MongoOperations mongoOperations;

    public MongoTeamRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Optional<Team> findById(String teamName) {
        return Optional.ofNullable(mongoOperations.findById(new Team.TeamCompositeKey(teamName),
                                                            Team.class,
                                                            COLLECTION_NAME));
    }

    @Override
    public Team save(Team team) {
        if (team.getId() == null) {
            team.setId(new Team.TeamCompositeKey(team.getName()));
        }
        return mongoOperations.save(team, COLLECTION_NAME);
    }

    @Override
    public boolean exists(String teamName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(teamName));
        return mongoOperations.exists(query, Team.class, COLLECTION_NAME);
    }
}
