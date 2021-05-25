package ru.hse.rekoder.exceptions;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String username) {
        super("The user with username \"" + username + "\" not found");
    }
}
