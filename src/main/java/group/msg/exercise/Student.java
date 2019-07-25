package group.msg.exercise;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="student_entity")
public class Student {
    @Id
    @GeneratedValue
    private int studentId;

    @Embedded
    private Address homeAddress;
    @ManyToOne
    private University university;

    private String firstName;
    private String lastName;
    private String section;


        @ManyToMany
        @JoinTable(name = "student_subject",
                joinColumns = @JoinColumn(name = "student_id"),
                inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

}
