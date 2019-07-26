package group.msg.examples.jpa.entity.day15;

import group.msg.examples.jpa.entity.mapping.MappedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "truck")

public  class Truck extends Vehicle {

    @Column(name = "loadCapacity")
    private int loadCapacity;

    @Column(name = "noOfContainers")
    private int noOfContainers;
}