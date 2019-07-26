package group.msg.examples.jpa.exercise_entityPackage;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "university_entity")
public class University implements Serializable {
    @Id
    @GeneratedValue
    private int university_id;
    private String name;
    private String country;

    @OneToMany(mappedBy = "university")
    private Collection<Student>students;
}
