package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;
import ru.hse.rekoder.model.Feedback;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SubmissionRequest {
    private String comment;
    @NotNull(message = "Specify source code")
    private String sourceCode;
    @NotNull(message = "Specify compiler")
    private String compiler;
    @Valid
    private Feedback feedback;
}
