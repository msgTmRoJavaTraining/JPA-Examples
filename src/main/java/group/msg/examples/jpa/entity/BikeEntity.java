package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class BikeEntity extends VehicleEntity {

    private int noOfPassengers;

    private int saddleHeight;
}
