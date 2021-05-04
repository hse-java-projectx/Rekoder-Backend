package ru.hse.rekoder.repositories;

import org.springframework.stereotype.Component;
import ru.hse.rekoder.model.ProblemOwner;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class ArrayProblemOwnerRepository implements ProblemOwnerRepository {
    private final ArrayList<ProblemOwner> owners = new ArrayList<>();

    @Override
    public Optional<ProblemOwner> findById(Integer id) {
        if (id == null || id < 0 || id >= owners.size()) {
            return Optional.empty();
        }
        return Optional.of(owners.get(id));
    }

    @Override
    public ProblemOwner save(ProblemOwner owner) {
        if (owner.getId() != null && owner.getId() >= 0 && owner.getId() < owners.size()) {
            owners.set(owner.getId(), owner);
            return owner;
        }
        owner.setId(owners.size());
        owners.add(owner);
        return owner;
    }
}
