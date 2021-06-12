package ru.hse.rekoder.exceptions;

public class SubmissionNotFoundException extends SubmissionException {
    public SubmissionNotFoundException(int submissionId) {
        super("The submission with id \"" + submissionId + "\" not found");
    }

    @Override
    public String getErrorType() {
        return "submission-not-found";
    }
}
