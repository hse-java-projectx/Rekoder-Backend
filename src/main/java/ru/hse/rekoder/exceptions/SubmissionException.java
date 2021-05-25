package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubmissionException extends  RuntimeException {
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
}
