package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super();
    }
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
    public AccessDeniedException(String message) {
        super(message);
    }
    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

}
