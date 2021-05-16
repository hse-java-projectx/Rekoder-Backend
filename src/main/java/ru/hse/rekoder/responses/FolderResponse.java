package ru.hse.rekoder.responses;

import ru.hse.rekoder.model.Folder;

public class FolderResponse extends BaseResponse {
    private final int id;
    private final String name;
    private final Integer parentFolderId;
    private final String ownerId;

    public FolderResponse(Folder originalFolder, String pathToResource) {
        super(pathToResource);
        this.id = originalFolder.getId();
        this.name = originalFolder.getName();
        this.parentFolderId = originalFolder.getParentFolder().getId();
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
