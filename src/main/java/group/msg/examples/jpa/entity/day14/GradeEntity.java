package group.msg.examples.jpa.entity.day14;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "grade_entity")
public class GradeEntity implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "grade")
    private int grade;

    @ManyToOne
    @JoinColumn(name = "student_entity")
    private StudentEntity studentEntity;

    @OneToOne
    @JoinColumn(name="subject_entity")
    private SubjectEntity subject;


}
