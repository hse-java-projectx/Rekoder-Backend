package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubmissionNotFoundException extends SubmissionException {
    public SubmissionNotFoundException(int submissionId) {
        super("The submission with id \"" + submissionId + "\" not found");
    }
}
