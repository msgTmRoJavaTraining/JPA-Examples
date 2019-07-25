package group.msg.exercise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @NotNull
    private String street;
    @NotNull
    private String city;
    @NotNull
    private String country;

}
