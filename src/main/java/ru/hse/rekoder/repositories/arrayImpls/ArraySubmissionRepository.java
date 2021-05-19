package ru.hse.rekoder.repositories.arrayImpls;

import org.springframework.stereotype.Component;
import ru.hse.rekoder.model.Submission;
import ru.hse.rekoder.repositories.SubmissionRepository;

import java.util.ArrayList;
import java.util.Optional;

public class ArraySubmissionRepository /*implements SubmissionRepository*/ {
    private final ArrayList<Submission> submissions = new ArrayList<>();

    //@Override
    public Optional<Submission> findById(Integer id) {
        if (id < 0 || id >= submissions.size()) {
            return Optional.empty();
        }
        return Optional.of(submissions.get(id));
    }

    //@Override
    public Submission save(Submission submission) {
        if (submission.getId() == null || submission.getId() < 0 || submission.getId() >= submissions.size()) {
            submission.setId(submissions.size());
            submissions.add(submission);
            return submission;
        }
        submissions.set(submission.getId(), submission);
        return submission;
    }
}
