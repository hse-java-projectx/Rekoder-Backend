package ru.hse.rekoder.exceptions;

public class ProblemNotFoundException extends ProblemException {
    public ProblemNotFoundException(int problemId) {
        super("The problem with id \"" + problemId + "\" not found");
    }

    @Override
    public String getErrorType() {
        return "problem-not-found";
    }
}
