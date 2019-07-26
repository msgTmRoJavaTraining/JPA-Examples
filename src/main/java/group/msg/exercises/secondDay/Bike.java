package group.msg.exercises.secondDay;

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
public class Bike extends Vehicle {

    private int saddleHeight;
    private int noOfPassengers;
}
