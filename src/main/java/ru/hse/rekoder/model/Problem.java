package ru.hse.rekoder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "problems")
public class Problem {
    public static final String SEQUENCE_NAME = "problem_sequence";
    @Id
    private Integer id;
    @NotEmpty(message = "Problem name must be not empty")
    private String name;

    @NotEmpty(message = "Specify not empty statement")
    private String statement;

    private List<@NotNull String> tags = new ArrayList<>();
    private List<@Valid @NotNull Test> tests = new ArrayList<>();

    private Integer originalProblemId;
    private ProblemOwner.CompositeKey ownerId;
    private String problemUrl;
    private int numberOfSuccessfulSubmissions = 0;

    public Integer getOriginalProblemId() {
        return originalProblemId;
    }

    public void setOriginalProblemId(Integer originalProblemId) {
        this.originalProblemId = originalProblemId;
    }

    public ProblemOwner.CompositeKey getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ProblemOwner.CompositeKey ownerId) {
        this.ownerId = ownerId;
    }

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
}
