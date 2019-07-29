package group.msg.examples.jpa.exercise_entityPackage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "student")
@CustomCityValidation
public class Student implements Serializable{
        @Id
        @GeneratedValue
        private int student_id;


        @NotNull(message = "Address can't be null")
        @Embedded
        private Address address;

        @CustomNameValidation(includeHardCodedValue = false)
        @NotNull(message = "First name can't be null")
        private String firstName;

        @NotNull(message = "Last name can't be null")
        private String lastName;
        @Email(message="E-mail is not valid")
        private String email;

        @ManyToOne(cascade= CascadeType.PERSIST)
        @JoinColumn(name = "university_id")
        private University university;

        @ManyToMany(cascade= CascadeType.PERSIST)
        @JoinTable(name = "student_subject",
                joinColumns = @JoinColumn(name = "student_id"),
                inverseJoinColumns = @JoinColumn(name = "subject_id"))
        private Collection<Subject> subjects;


        @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST},mappedBy = "student")
        private Collection<Grades> grades;

}


