package group.msg.exercise;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class University {
    @Id
    @GeneratedValue
    private int universityId;


    private String name;
    private String country;

    @OneToMany(mappedBy = "university")
    private List<Student> students;
}
