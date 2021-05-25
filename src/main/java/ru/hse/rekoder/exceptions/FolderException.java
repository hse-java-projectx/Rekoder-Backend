package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FolderException extends RuntimeException {
    public FolderException() {
        super();
    }
    public FolderException(String message, Throwable cause) {
        super(message, cause);
    }
    public FolderException(String message) {
        super(message);
    }
    public FolderException(Throwable cause) {
        super(cause);
    }
}
