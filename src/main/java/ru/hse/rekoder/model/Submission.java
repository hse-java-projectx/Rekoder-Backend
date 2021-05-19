package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "submissions")
public class Submission {
    @Transient
    public static final String SEQUENCE_NAME = "submission_sequence";

    @Id
    private Integer id;
    private Integer problemId;
    private String comment;

    @NotNull(message = "Source code must be not empty")
    private String sourceCode;
    @NotNull(message = "Compiler must be not empty")
    private String compiler;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" /* TODO,timezone=*/)
    private Date submissionTime;
    private ProblemOwner.CompositeKey authorId;
    @Valid
    private Feedback feedback;

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public ProblemOwner.CompositeKey getAuthorId() {
        return authorId;
    }

    public void setAuthorId(ProblemOwner.CompositeKey authorId) {
        this.authorId = authorId;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Date getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Date submissionTime) {
        this.submissionTime = submissionTime;
    }
}
