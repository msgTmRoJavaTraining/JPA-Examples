package group.msg.examples.jpa.entity.day14;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "subject_entity")
public class SubjectEntity implements Serializable {
    @Id
    @GeneratedValue
    private int subject_id;

    private String name;

    @ManyToMany
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Collection<StudentEntity> materiiStudent;

    @OneToOne(mappedBy = "subject")
    private GradesEntity grade;
}
