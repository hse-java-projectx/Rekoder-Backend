package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserException extends RuntimeException {
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
}
