package group.msg.examples.jpa.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Table
@Entity
public class Truck extends Vehicle
{
    private int loadCapacity;
    private int numberOfContainers;

}
