package ru.hse.rekoder.exceptions;

public class ProblemNotFoundException extends NotFoundException {
    public ProblemNotFoundException(int problemId) {
        super("The problem with id \"" + problemId + "\" not found");
    }
}
