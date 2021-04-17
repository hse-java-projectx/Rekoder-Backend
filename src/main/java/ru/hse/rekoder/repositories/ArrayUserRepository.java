package ru.hse.rekoder.repositories;

import org.springframework.stereotype.Component;
import ru.hse.rekoder.model.User;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class ArrayUserRepository implements UserRepository {
    private final ArrayList<User> users = new ArrayList<>();

    @Override
    public Optional<User> findById(Integer id) {
        if (id < 0 || id >= users.size()) {
            return Optional.empty();
        }
        return Optional.of(users.get(id));
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId() < 0 || user.getId() >= users.size()) {
            user.setId(users.size());
            users.add(user);
            return user;
        }
        users.set(user.getId(), user);
        return user;
    }

    @Override
    public long count() {
        return users.size();
    }
}
