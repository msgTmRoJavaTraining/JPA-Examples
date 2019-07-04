package group.msg.examples.jpa.entity.mapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("super_vague_entity")
public class SuperVagueEntity extends VagueEntity {

  @Column(name = "super_vague")
  private String superVague;
}
