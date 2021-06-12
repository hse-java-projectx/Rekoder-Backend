package ru.hse.rekoder.exceptions;

public class ProblemException extends RuntimeException implements ApiError {
    public ProblemException() {
        super();
    }
    public ProblemException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProblemException(String message) {
        super(message);
    }
    public ProblemException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getErrorType() {
        return "problem-error";
    }
}
