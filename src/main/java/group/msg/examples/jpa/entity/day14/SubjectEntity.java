package group.msg.examples.jpa.entity.day14;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Entity
@Table(name = "subject_entity")
public class SubjectEntity {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name="grade")
    private GradeEntity grade;

    @Version
    private long version;

    @NotNull
    @Column(name = "name")
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

    @ManyToMany
    @JoinTable(name = "student_to_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Collection<StudentEntity> studentList;
}
