package ru.hse.rekoder.requests;

import javax.validation.constraints.NotEmpty;

public class FolderRequest {
    @NotEmpty(message = "Folder name cannot be a empty string")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
