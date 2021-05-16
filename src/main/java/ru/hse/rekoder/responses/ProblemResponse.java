package ru.hse.rekoder.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Test;

import java.util.List;

public class ProblemResponse extends BaseResponse {
    private final int id;
    private final String name;
    private final String statement;
    private final List<Test> tests;
    private final List<String> tags;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String problemUrl;
    private final int numberOfSuccessfulSubmissions;

    public ProblemResponse(Problem originalProblem, String pathToResource) {
        super(pathToResource);
        this.id = originalProblem.getId();
        this.name = originalProblem.getName();
        this.statement = originalProblem.getStatement();
        this.tests = originalProblem.getTests();
        this.tags = originalProblem.getTags();
        this.problemUrl = originalProblem.getProblemUrl();
        this.numberOfSuccessfulSubmissions = originalProblem.getNumberOfSuccessfulSubmissions();
    }

    public List<Test> getTests() {
        return tests;
    }

    public ProblemResponse(Problem problem) {
        this(problem, "/problems/" + problem.getId());
    }

    public int getNumberOfSuccessfulSubmissions() {
        return numberOfSuccessfulSubmissions;
    }

    public String getProblemUrl() {
        return problemUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatement() {
        return statement;
    }

    public List<String> getTags() {
        return tags;
    }
}
