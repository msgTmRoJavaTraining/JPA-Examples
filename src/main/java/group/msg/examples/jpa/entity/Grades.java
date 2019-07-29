package group.msg.examples.jpa.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Setter @Getter
@Table

public class Grades
{
    @Id
    @GeneratedValue
    private int grade_id;

    private double valoare;

    @OneToOne()
    private Subject subject;

    @ManyToOne
    @JoinColumn(name="studentId")
    StudentEntity studentEntity;

}
