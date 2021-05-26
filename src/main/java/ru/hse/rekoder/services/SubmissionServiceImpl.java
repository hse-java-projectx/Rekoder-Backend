package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.SubmissionException;
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
    public Submission updateSubmission(Submission submission) {
        if (Objects.isNull(submission.getId())) {
            throw new SubmissionException("Submission must have an id");
        }
        return submissionRepository.update(submission, submission.getId())
                .orElseThrow(() -> new SubmissionNotFoundException(submission.getId()));
    }

    @Override
    public void deleteSubmission(int submissionId) {
        if (!submissionRepository.existsById(submissionId)) {
            throw new SubmissionNotFoundException(submissionId);
        }
        submissionRepository.deleteById(submissionId);
    }
}
