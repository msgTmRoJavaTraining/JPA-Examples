package group.msg.examples.jpa.entity.day15;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vehicle")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Vehicle {
    @Id
    @GeneratedValue
    private long id;

    private String manufacturer;

    private String vehicle_type;
}
