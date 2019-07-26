package group.msg.examples.jpa.entity;

import group.msg.examples.jpa.validator.CheckStudentsLocation;
import group.msg.examples.jpa.validator.IsThisStudentBannedOrNot;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@CheckStudentsLocation
@Table(name = "Students")
@Entity
@Data
public class StudentEntity {

    @Id
    @GeneratedValue
    private int student_id;

    @IsThisStudentBannedOrNot
    @NotNull(message = "First Name cannot be null!")
    private String first_name;

    @NotNull(message = "Last Name cannot be null!")
    private String last_name;

    private String section;

    @ManyToOne
    private UniversityEntity university;

    @Embedded
    private EmbeddableAddressEntity address;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<SubjectEntity> subjects;

    @OneToMany
    @JoinTable(name = "student_grades",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "grade_id"))
    private List<GradeEntity> grades;
}
