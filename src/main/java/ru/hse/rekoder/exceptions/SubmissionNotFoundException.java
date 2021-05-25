package ru.hse.rekoder.exceptions;

public class SubmissionNotFoundException extends NotFoundException {
    public SubmissionNotFoundException(int submissionId) {
        super("The submission with id \"" + submissionId + "\" not found");
    }
}
