package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FolderConflictException extends FolderException {
    public FolderConflictException(int parentFolderId, String duplicateName) {
        super("Duplicate subfolder name \"" + duplicateName + "\" in the folder with id " + parentFolderId);
    }
}
