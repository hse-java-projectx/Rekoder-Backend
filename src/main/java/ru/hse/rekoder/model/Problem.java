package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Document(collection = "problems")
public class Problem extends DocumentWithIncreasingIdSequence {
    @NotEmpty(message = "Problem name must be not empty")
    private String name;
    @NotNull(message = "Specify not empty statement")
    private String statement;

    private String inputFormat;
    private String outputFormat;

    private Set<@NotNull String> tags = new HashSet<>();
    private List<@Valid @NotNull Test> tests = new ArrayList<>();

    private Owner owner;

    private Integer originalProblemId;
    private String problemUrl;
}
