package group.msg.exercise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="grades")
public class Grade {
    @Id
    @GeneratedValue
    private int radeId;

    private double grd;

    @ManyToOne
    @JoinColumn(name="studentId")
    private Student student;

    @OneToOne
    private Subject subj;

}
