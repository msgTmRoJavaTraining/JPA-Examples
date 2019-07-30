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
@Table(name = "bike")
@DiscriminatorValue("subVehicle_bike")
public class Bike extends Vehicle {

    @Column(name = "no_of_passengers")
    private int noOfPassengers;

    @Column(name = "saddle_height")
    private int saddleHeight;
}
