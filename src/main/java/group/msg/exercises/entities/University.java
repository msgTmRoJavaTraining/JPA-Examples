package group.msg.exercises.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table
@Data
public class University {

    @Id
    @GeneratedValue
    private int university_id;

    @OneToMany(targetEntity=Student.class)
    private List<Student> student_list;

   @NotNull
    private String name;
    private String country;



    public University() {
        super();
    }


    public University(List<Student> student_list, String name, String country) {
        this.student_list = student_list;
        this.name = name;
        this.country = country;
    }
}