package ru.hse.rekoder.exceptions;

public class TeamNotFoundException extends TeamException {
    public TeamNotFoundException(String teamId) {
        super("The team with the id \"" + teamId + "\" not found");
    }

    @Override
    public String getErrorType() {
        return "team-not-found";
    }
}
