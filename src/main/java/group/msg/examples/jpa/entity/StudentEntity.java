package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class StudentEntity {

    @Id
    @GeneratedValue
    private int student_id;

    @NotNull(message = "First Name cannot be null!")
    private String first_name;

    @NotNull(message = "Last Name cannot be null!")
    private String last_name;

    private String section;

    @ManyToOne
    private UniversityEntity university;

    @Embedded
    private EmbeddableAddressEntity address;

    @ManyToMany
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<SubjectEntity> subjects;
}
