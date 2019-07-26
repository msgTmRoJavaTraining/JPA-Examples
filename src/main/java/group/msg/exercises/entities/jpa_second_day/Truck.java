package group.msg.exercises.entities.jpa_second_day;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table
@PrimaryKeyJoinColumn(name="ID")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Truck extends Vehicle {

    private int loadCapacity;
    private int numberOfContainers;
}
