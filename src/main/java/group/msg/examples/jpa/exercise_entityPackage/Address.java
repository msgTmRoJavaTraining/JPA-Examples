package group.msg.examples.jpa.exercise_entityPackage;

import lombok.Data;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Embeddable
public class Address implements Serializable {
    @NotNull(message = "Street can't be null")
    private String street;
    @NotNull(message = "City can't be null")
    private String city;
    @NotNull(message = "Country can't be null")
    private String country;

    public Address() {}

    public Address(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }
}
