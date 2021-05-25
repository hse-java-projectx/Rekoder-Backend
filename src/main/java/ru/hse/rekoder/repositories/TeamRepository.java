package ru.hse.rekoder.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team, ObjectId>, TeamModifyingRepository {
    Optional<Team> findByTeamId(String teamId);
    List<Team> findAllByTeamId(Iterable<String> teamId);
    List<Team> findAllByMemberIds(String ids);
    boolean existsByTeamId(String teamId);
}
