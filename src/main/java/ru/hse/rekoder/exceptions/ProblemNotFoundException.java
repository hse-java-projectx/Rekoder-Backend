package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProblemNotFoundException extends ProblemException {
    public ProblemNotFoundException(int problemId) {
        super("The problem with id \"" + problemId + "\" not found");
    }
}
