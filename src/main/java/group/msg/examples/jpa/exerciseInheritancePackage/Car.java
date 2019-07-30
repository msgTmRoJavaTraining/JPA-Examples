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
@Table(name = "car")
@DiscriminatorValue("subVehicle_car")
public class Car extends Vehicle {

    @Column(name = "no_of_passengers_for_car")
    private int noOfPassengers;

    @Column(name = "no_of_doors")
    private int noOfDoors;
}
