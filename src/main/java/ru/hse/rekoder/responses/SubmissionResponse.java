package ru.hse.rekoder.responses;

import lombok.Getter;
import ru.hse.rekoder.model.Feedback;
import ru.hse.rekoder.model.Submission;

import java.util.Date;

@Getter
public class SubmissionResponse {
    private final Integer id;
    private final Integer problemId;
    private final String comment;
    private final String sourceCode;
    private final String compiler;
    private final Date submissionTime;
    private final String authorId;
    private final Feedback feedback;

    public SubmissionResponse(Submission submission) {
        this.id = submission.getId();
        this.problemId = submission.getProblemId();
        this.comment = submission.getComment();
        this.sourceCode = submission.getSourceCode();
        this.compiler = submission.getCompiler();
        this.submissionTime = submission.getSubmissionTime();
        this.authorId = submission.getAuthorId();
        this.feedback = submission.getFeedback();
    }
}
