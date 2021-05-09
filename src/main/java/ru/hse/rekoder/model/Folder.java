package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "folder")
public class Folder {
    @Id
    private Integer id;
    @NotEmpty(message = "Folder name cannot be a empty string")
    private String name;
    @DBRef
    @JsonIgnore
    private Folder parentFolder;
    @DBRef
    private List<Folder> subfolders = new ArrayList<>();
    @DBRef
    private List<Problem> problems = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public List<Folder> getSubfolders() {
        return subfolders;
    }

    public void setSubfolders(List<Folder> subfolders) {
        this.subfolders = subfolders;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
}
