package group.msg.examples.jpa.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table
public class Bike extends Vehicle
{
    private int numberOfPassengers;
    private int saddleHeight;
}
