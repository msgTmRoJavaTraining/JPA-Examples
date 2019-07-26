package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class VehicleEntity {

    @Id
    @GeneratedValue
    private int idvehicle;

    private String manufacturer;

    private String vehicle_type;

}
