package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "problems")
public class Problem {
    @Id
    private Integer id;
    private String name;

    @NotEmpty(message = "Specify not empty statement")
    private String statement;

    private List<@NotNull String> tags = new ArrayList<>();
    private List<@NotNull Test> tests = new ArrayList<>();

    @DBRef
    @JsonManagedReference
    private List<Submission> submissions = new ArrayList<>();
    @DBRef
    private Problem originalProblem;
    @DBRef
    @JsonBackReference
    private ProblemOwner owner;
    private String problemUrl;
    private int numberOfSuccessfulSubmissions = 0;

    public int getNumberOfSuccessfulSubmissions() {
        return numberOfSuccessfulSubmissions;
    }

    public void setNumberOfSuccessfulSubmissions(int numberOfSuccessfulSubmissions) {
        this.numberOfSuccessfulSubmissions = numberOfSuccessfulSubmissions;
    }

    public String getProblemUrl() {
        return problemUrl;
    }

    public void setProblemUrl(String problemUrl) {
        this.problemUrl = problemUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

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
