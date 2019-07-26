package group.msg.examples.jpa.entity.day14;

import lombok.Data;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "grades_entity")
public class GradesEntity implements Serializable {
    @Id
    @GeneratedValue
    private long grade_id;

    private int grade;

    @ManyToOne
    @JoinColumn(name = "student_grades")
    private StudentEntity student;

    @OneToOne
    @JoinColumn(name = "subject")
    private SubjectEntity subject;
}
