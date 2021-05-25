package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Document(collection = "submissions")
public class Submission extends DocumentWithIncreasingIdSequence {
    private Integer problemId;
    private String comment;

    @NotNull(message = "Specify source code")
    private String sourceCode;
    @NotNull(message = "Specify compiler")
    private String compiler;
    private Date submissionTime;
    private String authorId;
    @Valid
    private Feedback feedback;
}
