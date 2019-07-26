package group.msg.examples.jpa.entity.day15;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "truck")
public class Truck extends Vehicle {
    private int loadCapacity;

    private int noOfContainers;
}
