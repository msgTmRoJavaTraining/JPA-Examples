package group.msg.examples.jpa.validator;

import lombok.Data;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.*;
import java.util.Date;

@Data
@Entity
@Table(name = "validation_entity")
public class ValidationEntity {

    @Id
    @GeneratedValue
    private int id;

    @NotNull(message = "Name can't be null")
    @Size(max = 20, message = "Name length must not be more than 20")
    private String name;

    @NotNull(message = "Please specify an e-mail")
    @Email(message = "E-mail is not valid")
    private String email;

    @Past
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;

    @Future
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Min(1)
    private int salary;

    @CustomValidation(includeHardCodedValue = false)
    private int noHardCodedValidation;

    @CustomValidation
    private int hardCodedValidation;

    @PrePersist
    @PreUpdate
    @PreRemove
    public void check() {
        if (salary > 10000 && !name.contains("Trump")) {
            throw new ValidationException("Salary is to high for this dude.");
        }
    }
}
