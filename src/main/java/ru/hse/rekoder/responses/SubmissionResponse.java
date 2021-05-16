package ru.hse.rekoder.responses;

import ru.hse.rekoder.model.Submission;

import java.util.Date;

public class SubmissionResponse extends BaseResponse {
    private final int id;
    private final int problemId;
    private final String comment;
    private final String sourceCode;
    private final String compiler;
    private final Date submissionTime;

    public SubmissionResponse(Submission originalSubmission, String pathToResource) {
        super(pathToResource);
        this.id = originalSubmission.getId();
        this.problemId = originalSubmission.getProblem().getId();
        this.comment = originalSubmission.getComment();
        this.sourceCode = originalSubmission.getSourceCode();
        this.compiler = originalSubmission.getCompiler();
        this.submissionTime = originalSubmission.getSubmissionTime();
    }

    public SubmissionResponse(Submission originalSubmission) {
        this(originalSubmission, "/submissions/" + originalSubmission.getId());
    }

    public int getId() {
        return id;
    }

    public int getProblemId() {
        return problemId;
    }

    public String getComment() {
        return comment;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public String getCompiler() {
        return compiler;
    }

    public Date getSubmissionTime() {
        return submissionTime;
    }
}
