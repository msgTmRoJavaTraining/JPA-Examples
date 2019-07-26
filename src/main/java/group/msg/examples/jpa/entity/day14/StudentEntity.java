package group.msg.examples.jpa.entity.day14;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "student_entity")
@CustomValidationStudentAddress
public class StudentEntity implements Serializable {
    @Id
    @GeneratedValue
    private int student_id;

    @Embedded
    @NotNull(message="Please specify an address")
    private AddressEntity home_address;

    @ManyToOne
    @JoinColumn(name = "many_Students_one_university")
    private UniversityEntity university_id;

    @NotNull(message="Please enter the first name of the student")
    @CustomValidationStudentName
    private String first_name;

    @NotNull(message="Please enter the last name of the student")
    private String last_name;

    private String section;

    @NotNull(message="Please specify an e-mail")
    @Email(message="E-mail is not valid")
    private String email;

    @ManyToMany
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Collection<SubjectEntity> manyToMany;
}
