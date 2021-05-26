package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.hse.rekoder.model.Team;

import java.util.Optional;

@Repository
public class ModifiableTeamRepositoryImpl implements ModifiableTeamRepository {
    private final MongoOperations mongoOperations;

    public ModifiableTeamRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Optional<Boolean> addUserToTeamById(String teamId, String userId) {
        Query teamById = Query.query(Criteria.where("teamId").is(teamId));
        Update update = new Update();
        update.addToSet("memberIds", userId);
        Team oldTeam = mongoOperations.findAndModify(teamById,
                update,
                FindAndModifyOptions.options().returnNew(false),
                Team.class);
        return Optional.ofNullable(oldTeam).map((team) -> !team.getMemberIds().contains(userId));
    }

    @Override
    public Optional<Boolean> deleteUserFromTeamById(String teamId, String userId) {
        Query teamById = Query.query(Criteria.where("teamId").is(teamId));
        Update update = new Update();
        update.pull("memberIds", userId);
        Team oldTeam = mongoOperations.findAndModify(teamById,
                update,
                FindAndModifyOptions.options().returnNew(false),
                Team.class);
        return Optional.ofNullable(oldTeam).map((team) -> team.getMemberIds().contains(userId));
    }
}
