package group.msg.examples.jpa.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Vehicle {
    @Id
    @GeneratedValue
    private int vehicle_id;

    private String manufactured;
    private String vehicleType;

}
