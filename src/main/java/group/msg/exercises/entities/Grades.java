package group.msg.exercises.entities;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grades {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @OneToOne
    private Subject subject;
    private int value;
    @Id
    @GeneratedValue
    private int id;
}
