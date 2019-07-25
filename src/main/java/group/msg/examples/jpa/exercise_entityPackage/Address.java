package group.msg.examples.jpa.exercise_entityPackage;

import lombok.Data;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class Address implements Serializable {
    private String street;
    private String city;
    private String country;

    public Address() {}

    public Address(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }
}
