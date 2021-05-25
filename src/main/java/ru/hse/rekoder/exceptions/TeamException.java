package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TeamException extends RuntimeException {
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
}
