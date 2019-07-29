package group.msg.examples.jpa.entity;

import group.msg.examples.jpa.validator.MyCustomValidator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class StudentEntity implements Serializable
{
    @Id
    @GeneratedValue
    private int student_id;


    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "universitate")
    private University university;


    @Embedded
    private HomeAddress homeAddress;

    @MyCustomValidator
    private String first_name;

    private String last_name;
    private String section;

    public StudentEntity(University university, HomeAddress homeAddress, String first_name, String last_name, String section) {
        this.university = university;
        this.homeAddress = homeAddress;
        this.first_name = first_name;
        this.last_name = last_name;
        this.section = section;
    }

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "studentEntity")
    private List<Grades> grades;

    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

    public StudentEntity(){}
}
