package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.ProblemOwner;

import java.util.Optional;

public interface ProblemOwnerRepository {
    Optional<ProblemOwner> findById(Integer id);
    ProblemOwner save(ProblemOwner newUser);
}
