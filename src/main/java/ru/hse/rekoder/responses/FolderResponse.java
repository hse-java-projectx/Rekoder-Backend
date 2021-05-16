package ru.hse.rekoder.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.ProblemOwner;

import java.util.Objects;

public class FolderResponse extends BaseResponse {
    private final int id;
    private final String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer parentFolderId;
    private final String ownerId;

    public FolderResponse(Folder originalFolder, String pathToResource) {
        super(pathToResource);
        this.id = originalFolder.getId();
        this.name = originalFolder.getName();
        Folder parentFolder = originalFolder.getParentFolder();
        this.parentFolderId = Objects.isNull(parentFolder) ? null : parentFolder.getId();
        this.ownerId = originalFolder.getOwner().getId().getProblemOwnerId();
    }

    public FolderResponse(Folder originalFolder) {
        this(originalFolder, "/folders/" + originalFolder.getId());
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getParentFolderId() {
        return parentFolderId;
    }
}
