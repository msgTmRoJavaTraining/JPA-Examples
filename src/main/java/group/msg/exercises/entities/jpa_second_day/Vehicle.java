package group.msg.exercises.entities.jpa_second_day;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table
@Inheritance(strategy= InheritanceType.JOINED)
@Data
@AllArgsConstructor
public class Vehicle {

    private int vehicleId;
    private String manufacturer;
    private String vehicle_type;


}
