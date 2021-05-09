package ru.hse.rekoder.repositories.arrayImpls;

import org.springframework.stereotype.Component;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.repositories.FolderRepository;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class ArrayFolderRepository implements FolderRepository {
    private final ArrayList<Folder> folders = new ArrayList<>();

    @Override
    public Optional<Folder> findById(Integer id) {
        if (id == null || id < 0 || id >= folders.size()) {
            return Optional.empty();
        }
        return Optional.of(folders.get(id));
    }

    @Override
    public Folder save(Folder folder) {
        if (folder.getId() != null) {
            folders.set(folder.getId(), folder);
            return folder;
        }
        folder.setId(folders.size());
        folders.add(folder);
        return folder;
    }
}
