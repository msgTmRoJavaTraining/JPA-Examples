package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class HomeAddress
{
    private String street;
    private String city;
    private String country;

    public HomeAddress(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public HomeAddress() {

    }
}
