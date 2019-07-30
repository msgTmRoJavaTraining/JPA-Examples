package group.msg.examples.jpa.exerciseEntityPackage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "grades_entity")
public class Grades {
    @Id
    @GeneratedValue
    private int idGrade;
    private int grade;

    @ManyToOne()
    @JoinColumn(name="studentID")
    private Student student;

    @OneToOne
    @JoinColumn(name= "subjectID")
    private Subject subject;
}
