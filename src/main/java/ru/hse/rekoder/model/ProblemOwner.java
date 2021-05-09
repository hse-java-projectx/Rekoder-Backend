package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "problemOwner")
public class ProblemOwner {
    protected static class CompositeKey {
        private String problemOwnerType;
        private String problemOwnerId;

        public CompositeKey(String problemOwnerType, String name) {
            this.problemOwnerType = problemOwnerType;
            this.problemOwnerId = name;
        }

        public String getProblemOwnerType() {
            return problemOwnerType;
        }

        public void setProblemOwnerType(String problemOwnerType) {
            this.problemOwnerType = problemOwnerType;
        }

        public String getProblemOwnerId() {
            return problemOwnerId;
        }

        public void setProblemOwnerId(String problemOwnerId) {
            this.problemOwnerId = problemOwnerId;
        }
    }

    @Id
    protected CompositeKey id;
    @DBRef
    @JsonManagedReference
    protected List<Problem> problems = new ArrayList<>();
    @DBRef
    protected List<Folder> topFolders;

    public List<Folder> getTopFolders() {
        return topFolders;
    }

    public void setTopFolders(List<Folder> topFolders) {
        this.topFolders = topFolders;
    }

    public CompositeKey getId() {
        return id;
    }

    public void setId(CompositeKey id) {
        this.id = id;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
}
