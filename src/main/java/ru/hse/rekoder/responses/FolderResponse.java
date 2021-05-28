package ru.hse.rekoder.responses;

import lombok.Getter;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Owner;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
public class FolderResponse {
    private final Integer id;
    private final String name;
    private final Integer parentFolderId;
    private final Owner owner;

    public FolderResponse(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.parentFolderId = folder.getParentFolderId();
        this.owner = folder.getOwner();
    }
}
