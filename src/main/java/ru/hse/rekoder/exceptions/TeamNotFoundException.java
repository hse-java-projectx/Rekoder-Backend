package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TeamNotFoundException extends TeamException {
    public TeamNotFoundException(String teamId) {
        super("The team with username \"" + teamId + "\" not found");
    }
}