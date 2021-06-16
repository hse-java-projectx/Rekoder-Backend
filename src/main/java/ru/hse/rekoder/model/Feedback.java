package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Feedback {
    @NotNull(message = "The verdict cannot be null")
    private String verdict;
    private String comment;
    private String timeConsumed;
    private String memoryConsumed;
}
