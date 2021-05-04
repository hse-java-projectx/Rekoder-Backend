package ru.hse.rekoder.services;

import ru.hse.rekoder.model.Submission;

public interface SubmissionService {
    Submission getSubmission(int submissionId);
    Submission addSubmission(int problemId, Submission submission);
}
