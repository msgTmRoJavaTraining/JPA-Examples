package group.msg.examples.jpa.validator;

import lombok.Data;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Entity
@Table(name = "validation_entity")
public class StudentNameValidationEntity
{

    @Id
    @GeneratedValue
    private int id;

    @NotNull(message = "Name can't be null")
    @Size(max = 20, message = "Name length must not be more than 20")
    private String name;

    @PrePersist
    @PreUpdate
    @PreRemove
    public void check() {
        if (!name.contains("Trump")) {
            throw new ValidationException("Ban this dude.");
        }
    }

}
