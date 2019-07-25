package group.msg.examples.jpa.exercise_entityPackage;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "subject_entity")
public class Subject implements Serializable {
    @Id
    @GeneratedValue
    private int subject_id;
    private String name;

    @ManyToMany
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Collection<Student> students;
}
