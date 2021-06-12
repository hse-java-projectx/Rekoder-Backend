package ru.hse.rekoder.exceptions;

public class TeamException extends RuntimeException implements ApiError {
    public TeamException() {
        super();
    }
    public TeamException(String message, Throwable cause) {
        super(message, cause);
    }
    public TeamException(String message) {
        super(message);
    }
    public TeamException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getErrorType() {
        return "team-error";
    }
}
