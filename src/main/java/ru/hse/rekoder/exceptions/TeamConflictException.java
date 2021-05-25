package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TeamConflictException extends TeamException {
    public TeamConflictException(String teamId) {
        super("The teamId \"" + teamId + "\" is already taken");
    }
}
