package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.Team;

import java.util.Optional;

public interface TeamModifyingRepository {
    Optional<Boolean> addUserToTeamById(Team.TeamCompositeKey teamId, String userId);
    Optional<Boolean> deleteUserFromTeamById(Team.TeamCompositeKey teamId, String userId);
}
