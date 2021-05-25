package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.Team;

import java.util.Optional;

public interface TeamModifyingRepository {
    Optional<Boolean> addUserToTeamById(String teamId, String userId);
    Optional<Boolean> deleteUserFromTeamById(String teamId, String userId);
}
