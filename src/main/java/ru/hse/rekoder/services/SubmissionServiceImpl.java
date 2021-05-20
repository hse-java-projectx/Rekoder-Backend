package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.SubmissionNotFoundException;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.SubmissionRepository;


@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    public Submission getSubmission(int submissionId) {
        return submissionRepository.findById(submissionId)
            .orElseThrow(() -> new SubmissionNotFoundException("Submission with id \"" + submissionId + "\" not found"));
    }

    @Override
    public void deleteSubmission(int submissionId) {
        //TODO decrease number of successful submissions in Problem
        if (submissionRepository.existsById(submissionId)) {
            throw new SubmissionNotFoundException("Submission not found");
        }
        submissionRepository.deleteById(submissionId);
    }
}
