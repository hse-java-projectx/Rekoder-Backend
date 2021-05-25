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
}
