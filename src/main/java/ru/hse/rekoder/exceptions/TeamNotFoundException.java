package ru.hse.rekoder.exceptions;

public class TeamNotFoundException extends NotFoundException {
    public TeamNotFoundException(String teamId) {
        super("The team with username \"" + teamId + "\" not found");
    }
}
