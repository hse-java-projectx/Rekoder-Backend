package ru.hse.rekoder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Owner {
    private ContentGeneratorType type;
    private String id;

    public static Owner userWithId(String id) {
        return new Owner(ContentGeneratorType.USER, id);
    }

    public static Owner teamWithId(String id) {
        return new Owner(ContentGeneratorType.TEAM, id);
    }
}
