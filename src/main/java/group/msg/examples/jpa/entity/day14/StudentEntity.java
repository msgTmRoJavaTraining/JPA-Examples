package group.msg.examples.jpa.entity.day14;


import group.msg.examples.jpa.entity.EmbeddableEntity;
import group.msg.examples.jpa.entity.SimpleType;
import group.msg.examples.jpa.entity.mapping.ManyEntity;
import group.msg.examples.jpa.entity.mapping.OneEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "student_entity")
public class StudentEntity implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Version
    private long version;

    @ManyToOne
    @JoinColumn(name = "one_university")
    private UniversityEntity university_id;

    @NotNull
    @Column(name = "first_name")
    private String first_name;

    @NotNull(message="Please specify an e-mail")
    @Email(message="E-mail is not valid")
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "last_name")
    private String last_name;

    @NotNull
    @Column(name = "section")
    private String section;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

    @Enumerated(EnumType.STRING)
    private SimpleType type;

    @Embedded
    private AdressEmbeddableEntity address;

    @ManyToMany
    @JoinTable(name = "student_to_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Collection<SubjectEntity> subjectList;
}
