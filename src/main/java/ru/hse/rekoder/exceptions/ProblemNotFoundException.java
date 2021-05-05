package ru.hse.rekoder.exceptions;

public class ProblemNotFoundException extends RuntimeException {
    public ProblemNotFoundException() {
    }

    public ProblemNotFoundException(String message) {
        super(message);
    }

    public ProblemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
