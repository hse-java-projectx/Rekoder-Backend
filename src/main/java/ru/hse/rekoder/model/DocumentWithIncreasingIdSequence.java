package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public abstract class DocumentWithIncreasingIdSequence {
    @Id
    private Integer id;
}
