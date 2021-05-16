package ru.hse.rekoder.exceptions;

public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException() {
        super();
    }

    public FolderNotFoundException(String message) {
        super(message);
    }

    public FolderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
