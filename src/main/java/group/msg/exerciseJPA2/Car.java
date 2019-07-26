package group.msg.exerciseJPA2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="car")
public class Car extends Vehicle {
    private int noOfDors;
    private int noOfPassenger;
}
