package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "Universities")
public class UniversityEntity {

    @Id
    @GeneratedValue
    private String university_id;

    @OneToMany(mappedBy = "university")
    private List<StudentEntity> students;

    @NotNull(message = "A university must have a name!")
    private String name;

    @NotNull(message = "A university must belong to a country!")
    private String country;


}
