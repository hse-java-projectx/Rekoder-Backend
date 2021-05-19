package ru.hse.rekoder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "folders")
public class Folder {
    @Transient
    public static final String SEQUENCE_NAME = "folder_sequence";
    @Id
    private Integer id;
    @NotEmpty(message = "Folder name cannot be a empty string")
    private String name;
    private Integer parentFolderId;
    private Set<@NotNull Integer> problemIds = new HashSet<>();
    private ProblemOwner.CompositeKey ownerId;

    public Integer getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Integer parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public Set<Integer> getProblemIds() {
        return problemIds;
    }

    public void setProblemIds(Set<Integer> problemIds) {
        this.problemIds = problemIds;
    }

    public ProblemOwner.CompositeKey getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ProblemOwner.CompositeKey ownerId) {
        this.ownerId = ownerId;
    }

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
}
