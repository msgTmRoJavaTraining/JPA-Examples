package group.msg.examples.jpa.entity.day14;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Entity
@Table(name = "subject_entity")
public class SubjectEntity {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne(mappedBy = "subject")
    @JoinColumn(name="grade")
    private GradeEntity grade;

    @NotNull
    @Column(name = "name")
    private String name;


    @ManyToMany
    @JoinTable(name = "student_to_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Collection<StudentEntity> studentList;
}
