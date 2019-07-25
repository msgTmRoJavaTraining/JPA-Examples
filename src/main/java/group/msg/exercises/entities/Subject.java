package group.msg.exercises.entities;

import group.msg.examples.jpa.entity.mapping.ManyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

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



    @ManyToMany
    @JoinTable(name = "many_many",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Collection<Student> manyToManyStudent;



    }

