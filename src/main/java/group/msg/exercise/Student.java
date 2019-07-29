package group.msg.exercise;

import group.msg.examples.jpa.validator.BannedNames;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Data
@Table(name="student_entity")
public class Student {
    @Id
    @GeneratedValue
    private int studentId;

    @NotNull
    @Embedded
    private Address homeAddress;
    @ManyToOne
    private University university;

    @BannedNames
    private String firstName;

    private String lastName;
    private String section;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "student")
    private List <Grade>grades;



        @ManyToMany
        @JoinTable(name = "student_subject",
                joinColumns = @JoinColumn(name = "student_id"),
                inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;


}
