package ru.hse.rekoder.exceptions;

public class FolderNotFoundException extends NotFoundException {
    public FolderNotFoundException(int folderId) {
        super("The folder with id \"" + folderId + "\" not found");
    }
}
