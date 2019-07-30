package group.msg.examples.jpa.exerciseInheritancePackage;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "truck")
@DiscriminatorValue("subVehicle_truck")
public class Truck extends Vehicle {

    @Column(name = "load_capacity")
    private int loadCapacity;

    @Column(name = "no_of_containers")
    private int noOfContainers;
}
