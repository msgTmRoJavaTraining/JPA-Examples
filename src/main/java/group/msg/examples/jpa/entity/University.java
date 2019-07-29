package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table
@Entity
public class University {

    @Id
    @GeneratedValue
    private int university_id;

    @OneToMany(mappedBy = "university")
    private List<StudentEntity> studentEntityList;

    private String name;
    private String country;


    public University(List<StudentEntity> studentEntityList, String name, String country) {
        this.studentEntityList = studentEntityList;
        this.name = name;
        this.country = country;
    }

    public University() {

    }
}
