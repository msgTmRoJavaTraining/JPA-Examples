package group.msg.exercises.entities;



import group.msg.examples.jpa.entity.mapping.OneEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Entity
@Table
@Data
public class Student {
    @Id
    @GeneratedValue
    private int student_id;

    @Embedded

    private Adress adress;

    @ManyToOne
    private University university_id;

    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Grades> grades;

    private String first_name;


    private String last_name;
    private String section;


    @ManyToMany
    @JoinTable(name = "Student_Subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))

    private Collection<Subject> manyToManySubject;


    public Student() {
        super();
    }

    public Student(Adress adress, University university_id, String first_name, String last_name, String section, Collection<Subject> manyToManySubject) {
        this.adress = adress;
        this.university_id = university_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.section = section;
        this.manyToManySubject = manyToManySubject;
    }
}