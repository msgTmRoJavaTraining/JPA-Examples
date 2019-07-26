package group.msg.exercises.entities.jpa_second_day;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Inheritance(strategy= InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue
    private int vehicleId;
    private String manufacturer;
    private String vehicle_type;


}
