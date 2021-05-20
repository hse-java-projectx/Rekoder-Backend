package ru.hse.rekoder.requests;

import ru.hse.rekoder.model.Feedback;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class SubmissionRequest {
    private String comment;
    @NotNull(message = "Source code must be not empty")
    private String sourceCode;
    @NotNull(message = "Compiler must be not empty")
    private String compiler;
    @Valid
    private Feedback feedback;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getCompiler() {
        return compiler;
    }

    public void setCompiler(String compiler) {
        this.compiler = compiler;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}
