package ru.hse.rekoder.repositories;

import ru.hse.rekoder.model.Folder;

import java.util.List;

public interface FolderGraphLookupRepository {
    List<Integer> getSubTree(int folderId);
}
