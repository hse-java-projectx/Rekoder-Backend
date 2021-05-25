package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProblemException extends RuntimeException {
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
}
