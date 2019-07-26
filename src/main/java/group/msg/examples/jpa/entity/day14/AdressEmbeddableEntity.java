package group.msg.examples.jpa.entity.day14;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Data
@Embeddable
public class AdressEmbeddableEntity {

    @NotNull
    @Column(name = "street")
    private String street;

    @NotNull
    @Column(name = "city")
    private String city;

    @NotNull

    @Column(name = "country")
    private String country;

    public AdressEmbeddableEntity() {}

    public AdressEmbeddableEntity(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }
}
