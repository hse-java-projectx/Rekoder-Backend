package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "submissions")
public class Submission {
    @Id
    private Integer id;
    @DBRef
    @JsonBackReference
    private Problem problem;
    private String comment;

    @NotNull(message = "Source code must be not empty")
    private String sourceCode;
    @NotNull(message = "Compiler must be not empty")
    private String compiler;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" /* TODO,timezone=*/)
    private Date submissionTime;
    @DBRef
    private ProblemOwner author;
    @Valid
    private Feedback feedback;

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public ProblemOwner getAuthor() {
        return author;
    }

    public void setAuthor(ProblemOwner author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
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
