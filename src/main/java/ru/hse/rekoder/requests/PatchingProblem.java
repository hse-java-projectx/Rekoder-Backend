package ru.hse.rekoder.requests;

import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Test;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PatchingProblem {
    @NotEmpty(message = "Problem name must be not empty")
    private String name;
    @NotEmpty(message = "Specify not empty statement")
    private String statement;
    private List<@NotNull String> tags = new ArrayList<>();
    private List<@Valid @NotNull Test> tests = new ArrayList<>();
    private String problemUrl;

    public PatchingProblem() {}

    public PatchingProblem(Problem originalProblem) {
        this.name = originalProblem.getName();
        this.statement = originalProblem.getStatement();
        this.tags = originalProblem.getTags();
        this.tests = originalProblem.getTests();
        this.problemUrl = originalProblem.getProblemUrl();
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

    public String getProblemUrl() {
        return problemUrl;
    }

    public void setProblemUrl(String problemUrl) {
        this.problemUrl = problemUrl;
    }
}
