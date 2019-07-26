package group.msg.examples.jpa.exercise_InheritancePackage;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@MappedSuperclass
@Table(name = "vehicle")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Vehicle {
    @Id
    @GeneratedValue
    private long idVehicle;

    private String manufacturer;
    private String vehicle_type;
}
