package ru.hse.rekoder.responses;

import lombok.Getter;
import lombok.Setter;
import ru.hse.rekoder.model.ContentGenerator;

import java.util.Date;

@Getter
public class ContentGeneratorResponse {
    protected String name;
    protected String bio;
    protected Integer rootFolderId;
    protected Date registrationDate;

    protected ContentGeneratorResponse(ContentGenerator contentGenerator) {
        this.name = contentGenerator.getName();
        this.bio = contentGenerator.getBio();
        this.rootFolderId = contentGenerator.getRootFolderId();
        this.registrationDate = contentGenerator.getRegistrationDate();
    }
}
