package ru.hse.rekoder.responses;

import ru.hse.rekoder.model.Problem;

import java.util.List;

public class ProblemResponse extends BaseResponse {
    private final int id;
    private final String name;
    private final String statement;
    //private final List<Test> tests;
    private final List<String> tags;

    public ProblemResponse(Problem originalProblem, String pathToResource) {
        super(pathToResource);
        this.id = originalProblem.getId();
        this.name = originalProblem.getName();
        this.statement = originalProblem.getStatement();
        //this.tests = originalProblem.getTests();
        this.tags = originalProblem.getTags();
    }

    public ProblemResponse(Problem problem) {
        this(problem, "/problems/" + problem.getId());
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
