package ru.hse.rekoder.model;

import javax.validation.constraints.NotEmpty;

public class Feedback {
    @NotEmpty(message = "Verdict cannot be empty")
    private String verdict;
    private String comment;
    private String timeConsumed;
    private String memoryConsumed;

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimeConsumed() {
        return timeConsumed;
    }

    public void setTimeConsumed(String timeConsumed) {
        this.timeConsumed = timeConsumed;
    }

    public String getMemoryConsumed() {
        return memoryConsumed;
    }

    public void setMemoryConsumed(String memoryConsumed) {
        this.memoryConsumed = memoryConsumed;
    }
}
