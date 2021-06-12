package ru.hse.rekoder.exceptions;

public class FolderException extends RuntimeException implements ApiError {
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

    @Override
    public String getErrorType() {
        return "folder-error";
    }
}
