package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.ContentGenerator;

import java.util.Optional;

public interface ProblemOwnerRepository {
    Optional<ContentGenerator> findById(Integer id);
    ContentGenerator save(ContentGenerator newUser);
}
