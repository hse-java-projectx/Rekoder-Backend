package ru.hse.rekoder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.rekoder.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team, Team.TeamCompositeKey> {
    Optional<Team> findById(Team.TeamCompositeKey teamName);
    List<Team> findAllById(Iterable<Team.TeamCompositeKey> id);
    boolean existsById(Team.TeamCompositeKey id);
}
