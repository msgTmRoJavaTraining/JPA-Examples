package group.msg.exercises.entities.jpa_second_day;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table
@PrimaryKeyJoinColumn
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car extends Vehicle {

    private int noOfPassenger;
    private int noOfDoors;
}
