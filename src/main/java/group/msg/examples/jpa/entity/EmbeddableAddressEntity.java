package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Data
@Embeddable
public class EmbeddableAddressEntity {

    private int address_id;

    @NotNull
    private String country;

    @NotNull
    private String city;

    @NotNull
    private String street;
}
