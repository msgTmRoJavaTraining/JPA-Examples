package group.msg.examples.jpa.entity.day14;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class AddressEntity {
    private String street;
    private String city;
    private String country;

    public AddressEntity() {
    }

    public AddressEntity(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }
}
