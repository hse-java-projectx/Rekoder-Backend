package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class Problem {
    private Integer id;
    private String name;

    @NotEmpty(message = "Specify not empty statement")
    private String statement;
    @JsonManagedReference
    private List<Submission> submissions = new ArrayList<>();
    private Problem originalProblem;
    @JsonBackReference
    private ProblemOwner owner;

    public ProblemOwner getOwner() {
        return owner;
    }

    public void setOwner(ProblemOwner owner) {
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public Problem getOriginalProblem() {
        return originalProblem;
    }

    public void setOriginalProblem(Problem originalProblem) {
        this.originalProblem = originalProblem;
    }
}
