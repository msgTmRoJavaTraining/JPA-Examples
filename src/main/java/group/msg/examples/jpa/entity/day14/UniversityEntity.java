package group.msg.examples.jpa.entity.day14;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Table(name="university_entity")
public class UniversityEntity {
    @Id
    @GeneratedValue
    private int university_id;

    @OneToMany(mappedBy = "university_id")
    private Collection<StudentEntity> oneToManyStudents;

    private String name;

    private String country;
}
