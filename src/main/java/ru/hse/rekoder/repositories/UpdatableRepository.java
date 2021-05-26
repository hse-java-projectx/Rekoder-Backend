package ru.hse.rekoder.repositories;

import java.util.Optional;

public interface UpdatableRepository<T, ID> {
    Optional<T> update(T entity, ID id);
}
