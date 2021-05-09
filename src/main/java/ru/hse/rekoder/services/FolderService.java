package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Folder;

public interface FolderService {
    Folder getFolder(int folderId);
    Folder createNewFolder(int parentFolderId, Folder folder);
}
