package ru.hse.rekoder.exceptions;

public class FolderConflictException extends FolderException {
    public FolderConflictException(int parentFolderId, String duplicateName) {
        super("Duplicate subfolder name \"" + duplicateName + "\" in the folder with id " + parentFolderId);
    }

    @Override
    public String getErrorType() {
        return "folder-conflict";
    }
}
