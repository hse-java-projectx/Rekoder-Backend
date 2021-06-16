package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Test {
    @NotNull(message = "The input data cannot be null")
    private String input;
    @NotNull(message = "The input data cannot be null")
    private String output;
}
