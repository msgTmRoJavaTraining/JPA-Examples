package group.msg.examples.jpa.entity;

import group.msg.examples.jpa.entity.mapping.ManyEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
public class SubjectEntity {
    @Id
    private int subject_id;

    private String name;

    @ManyToMany
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<StudentEntity> students;
}
