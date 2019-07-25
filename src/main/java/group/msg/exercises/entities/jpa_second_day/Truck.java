package group.msg.exercises.entities.jpa_second_day;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table
@PrimaryKeyJoinColumn(name="ID")
public class Truck extends Vehicle {

    private int loadCapacity;
    private int numberOfContainers;
}
