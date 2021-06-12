package ru.hse.rekoder.exceptions;

public class FolderNotFoundException extends FolderException {
    public FolderNotFoundException(int folderId) {
        super("The folder with id \"" + folderId + "\" not found");
    }

    @Override
    public String getErrorType() {
        return "folder-not-found";
    }
}
