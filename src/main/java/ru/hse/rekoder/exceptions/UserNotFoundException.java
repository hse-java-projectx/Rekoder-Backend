package ru.hse.rekoder.exceptions;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(String username) {
        super("The user with username \"" + username + "\" not found");
    }

    @Override
    public String getErrorType() {
        return "user-not-found";
    }
}
