package group.msg.examples.jpa.entity.day15;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "car")
public class Car extends Vehicle{
    private int noOfPassengers;

    private int noOfDoors;
}
