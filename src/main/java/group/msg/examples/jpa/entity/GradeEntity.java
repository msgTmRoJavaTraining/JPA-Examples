package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.jar.Attributes;

@Entity
@Data
@Table(name = "GRADES")
public class GradeEntity {

    @Id
    @GeneratedValue
    private int grade_id;

    @NotNull
    @Min(value = 1, message = "Grade couldn't be smaller than 1!")
    @Max(value = 10, message = "Grade couldn't be bigger than 10!")
    private double grade;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @OneToOne
    private SubjectEntity subject;

}
