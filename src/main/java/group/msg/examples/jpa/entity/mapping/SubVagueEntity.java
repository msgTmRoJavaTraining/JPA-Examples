package group.msg.examples.jpa.entity.mapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("sub_vague_entity")
public class SubVagueEntity extends VagueEntity {

  @Column(name = "sub_vague")
  private String subVague;
}
