package ru.hse.rekoder.model;

import javax.validation.constraints.NotNull;

public class Test {
    @NotNull(message = "Specify input data")
    private String input;
    @NotNull(message = "Specify input data")
    private String output;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
