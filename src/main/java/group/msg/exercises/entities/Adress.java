package group.msg.exercises.entities;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class Adress {

    private int adress_id;
    @NotNull
    private String street;
    @NotNull
    private String city;
    @NotNull
    private String country;



    public Adress() {
        super();
    }

    public Adress(int adress_id, String street, String city, String country) {
        this.adress_id = adress_id;
        this.street = street;
        this.city = city;
        this.country = country;
    }


}
