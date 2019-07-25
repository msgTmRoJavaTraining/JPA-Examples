package group.msg.exercise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class University {
    @Id
    @GeneratedValue
    private int universityId;


    private String name;
    private String country;

    @OneToMany(mappedBy = "university")
    private List<Student> students;
}
