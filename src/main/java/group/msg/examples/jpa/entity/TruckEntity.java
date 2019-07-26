package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class TruckEntity extends VehicleEntity {

    private int loadCapacity;

    private int noOfContainers;

}
