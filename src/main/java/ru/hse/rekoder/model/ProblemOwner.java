package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "problem_owners")
public class ProblemOwner {
    public static class CompositeKey {
        private String problemOwnerType;
        private String problemOwnerId;

        public CompositeKey(String problemOwnerType, String name) {
            this.problemOwnerType = problemOwnerType;
            this.problemOwnerId = name;
        }

        public CompositeKey() {}

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
    protected Integer rootFolderId;

    public Integer getRootFolderId() {
        return rootFolderId;
    }

    public void setRootFolderId(Integer rootFolderId) {
        this.rootFolderId = rootFolderId;
    }

    public CompositeKey getId() {
        return id;
    }

    public void setId(CompositeKey id) {
        this.id = id;
    }
}
