package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Submission;

public interface SubmissionService {
    Submission getSubmission(int submissionId);
    void deleteSubmission(int submissionId);
}
