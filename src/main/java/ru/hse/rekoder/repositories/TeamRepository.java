package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.Team;

import java.util.Optional;

public interface TeamRepository {
    Optional<Team> findById(String teamName);
    Team save(Team team);
    boolean exists(String teamName);
}
