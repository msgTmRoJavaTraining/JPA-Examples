package group.msg.exercise;

import lombok.Data;

import javax.persistence.Embeddable;



@Embeddable
@Data
public class Address {
    private String street;
    private String city;
    private String country;

}
