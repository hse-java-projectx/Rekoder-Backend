package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FolderNotFoundException extends FolderException {
    public FolderNotFoundException(int folderId) {
        super("The folder with id \"" + folderId + "\" not found");
    }
}
