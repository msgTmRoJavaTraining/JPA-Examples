package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class CarEntity extends VehicleEntity {

    private int noOfPassengers;

    private int noOfDoors;
}
