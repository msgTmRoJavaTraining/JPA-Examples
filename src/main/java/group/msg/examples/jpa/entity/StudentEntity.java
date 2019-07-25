package group.msg.examples.jpa.entity;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table
public class StudentEntity implements Serializable
{
    @Id
    @GeneratedValue
    private int student_id;

    @ManyToOne
    @JoinColumn(name = "universitate")
    private University university;

    @Embedded
    private HomeAddress homeAddress;

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

    @ManyToMany
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

    public StudentEntity(){}
}
