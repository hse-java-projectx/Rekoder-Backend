package ru.hse.rekoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserConflictException extends UserException {
    public UserConflictException(String username) {
        super("The username \"" + username + "\" is already taken");
    }
}
