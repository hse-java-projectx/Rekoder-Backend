package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ContentGenerator extends DocumentWithIncreasingIdSequence {
    protected String name;
    protected String bio;
    protected Integer rootFolderId;
    protected Date registrationDate;
}
