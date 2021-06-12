package ru.hse.rekoder.exceptions;

public class UserConflictException extends UserException {
    public UserConflictException(String username) {
        super("The username \"" + username + "\" is already taken");
    }

    @Override
    public String getErrorType() {
        return "user-conflict";
    }
}
