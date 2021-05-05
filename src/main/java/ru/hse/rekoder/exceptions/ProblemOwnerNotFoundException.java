package ru.hse.rekoder.exceptions;

public class ProblemOwnerNotFoundException extends RuntimeException {
    public ProblemOwnerNotFoundException() {
        super();
    }

    public ProblemOwnerNotFoundException(String message) {
        super(message);
    }

    public ProblemOwnerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
