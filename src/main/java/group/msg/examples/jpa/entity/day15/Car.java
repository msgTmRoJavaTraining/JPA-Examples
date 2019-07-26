package group.msg.examples.jpa.entity.day15;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "car")



public  class Car extends Vehicle {

    @Column(name = "noOfPassegers")
    private int noOfPassegers;

    @Column(name = "noOfDoors")
    private int noOfDoors;
}