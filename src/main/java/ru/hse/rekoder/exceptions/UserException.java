package ru.hse.rekoder.exceptions;

public class UserException extends RuntimeException implements ApiError {
    public UserException() {
        super();
    }
    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserException(String message) {
        super(message);
    }
    public UserException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getErrorType() {
        return "user-error";
    }
}
