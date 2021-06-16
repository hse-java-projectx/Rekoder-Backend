package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;
import ru.hse.rekoder.model.Test;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProblemRequest {
    @NotEmpty(message = "The problem name must be not empty")
    private String name;
    @NotNull(message = "The statement must not be null")
    private String statement;
    private String inputFormat;
    private String outputFormat;
    @NotNull(message = "The tags must not be null if they are specified")
    private Set<@NotNull String> tags = new HashSet<>();
    @NotNull(message = "The tests must not be null if they are specified")
    private List<@Valid @NotNull Test> tests = new ArrayList<>();
    private String problemUrl;
}
