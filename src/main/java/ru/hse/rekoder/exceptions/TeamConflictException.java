package ru.hse.rekoder.exceptions;

public class TeamConflictException extends TeamException {
    public TeamConflictException(String teamId) {
        super("The teamId \"" + teamId + "\" is already taken");
    }

    @Override
    public String getErrorType() {
        return "team-conflict";
    }
}
