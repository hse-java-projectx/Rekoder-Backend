package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

public class ProblemOwner {
    protected Integer id;
    @JsonManagedReference
    protected List<Problem> problems = new ArrayList<>();

    protected List<Folder> topFolders;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
}
