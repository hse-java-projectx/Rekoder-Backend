package ru.hse.rekoder.exceptions;

public class SubmissionException extends RuntimeException implements ApiError {
    public SubmissionException() {
        super();
    }
    public SubmissionException(String message, Throwable cause) {
        super(message, cause);
    }
    public SubmissionException(String message) {
        super(message);
    }
    public SubmissionException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getErrorType() {
        return "submission-error";
    }
}
