package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Test {
    @NotNull(message = "Specify input data")
    private String input;
    @NotNull(message = "Specify output data")
    private String output;
}
