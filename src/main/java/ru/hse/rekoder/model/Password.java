package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Document(collection = "passwords")
public class Password {
    @Id
    ObjectId id;
    @NotNull(message = "The password must not be null")
    @Size(min = 5, message = "The password length must be greater than 5")
    private String stringPassword;
}
