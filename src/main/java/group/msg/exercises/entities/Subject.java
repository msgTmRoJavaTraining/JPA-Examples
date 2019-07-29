package group.msg.exercises.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table
@Data
public class Subject {

    @Id
    @GeneratedValue
    private int subject_id;
    private String name;
    @OneToOne
    private  Grades grade;


    @ManyToMany
    @JoinTable(name = "Student_Subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> manyToManyStudent;



}