package ru.hse.rekoder.responses;

import lombok.Getter;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.model.Test;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Getter
public class ProblemResponse {
    private final Integer id;
    private final String name;
    private final String statement;
    private final String inputFormat;
    private final String outputFormat;
    private final Set<@NotNull String> tags;
    private final List<@Valid @NotNull Test> tests;
    private final Owner owner;
    private final Integer originalProblemId;
    private final String problemUrl;

    public ProblemResponse(Problem problem) {
        this.id = problem.getId();
        this.name = problem.getName();
        this.statement = problem.getStatement();
        this.inputFormat = problem.getInputFormat();
        this.outputFormat = problem.getOutputFormat();
        this.tags = problem.getTags();
        this.tests = problem.getTests();
        this.owner = problem.getOwner();
        this.originalProblemId = problem.getOriginalProblemId();
        this.problemUrl = problem.getProblemUrl();
    }
}
