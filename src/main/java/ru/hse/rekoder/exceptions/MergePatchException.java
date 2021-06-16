package ru.hse.rekoder.exceptions;

public class MergePatchException extends RuntimeException {
    public MergePatchException() {
        super();
    }
    public MergePatchException(String message, Throwable cause) {
        super(message, cause);
    }
    public MergePatchException(String message) {
        super(message);
    }
    public MergePatchException(Throwable cause) {
        super(cause);
    }
}
