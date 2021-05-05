package ru.hse.rekoder.exceptions;

public class SubmissionNotFoundException extends RuntimeException {
    public SubmissionNotFoundException() {
    }

    public SubmissionNotFoundException(String message) {
        super(message);
    }

    public SubmissionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
