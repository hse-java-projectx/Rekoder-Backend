package ru.hse.rekoder.repositories.arrayImpls;

import ru.hse.rekoder.model.ContentGenerator;
import ru.hse.rekoder.repositories.ProblemOwnerRepository;

import java.util.ArrayList;
import java.util.Optional;

public class ArrayProblemOwnerRepository implements ProblemOwnerRepository {
    private final ArrayList<ContentGenerator> owners = new ArrayList<>();

    @Override
    public Optional<ContentGenerator> findById(Integer id) {
        if (id == null || id < 0 || id >= owners.size()) {
            return Optional.empty();
        }
        return Optional.of(owners.get(id));
    }

    @Override
    public ContentGenerator save(ContentGenerator owner) {
        /*if (owner.getId() != null && owner.getId() >= 0 && owner.getId() < owners.size()) {
            owners.set(owner.getId(), owner);
            return owner;
        }
        owner.setId(owners.size());
        owners.add(owner);
        return owner;
         */
        return null;
    }
}
