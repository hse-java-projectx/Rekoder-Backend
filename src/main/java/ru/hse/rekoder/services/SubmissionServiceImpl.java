package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.SubmissionNotFoundException;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.SubmissionRepository;

import java.util.Objects;


@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    public Submission getSubmission(int submissionId) {
        return submissionRepository.findById(submissionId)
            .orElseThrow(() -> new SubmissionNotFoundException(submissionId));
    }

    @Override
    public Submission updateSubmission(Submission Submission) {
        if (Objects.isNull(Submission.getId())) {
            throw new RuntimeException("Submission must have an id");
        }
        return submissionRepository.save(Submission);
    }

    @Override
    public void deleteSubmission(int submissionId) {
        if (!submissionRepository.existsById(submissionId)) {
            throw new SubmissionNotFoundException(submissionId);
        }
        submissionRepository.deleteById(submissionId);
    }
}
