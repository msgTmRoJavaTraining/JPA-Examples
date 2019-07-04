package group.msg.examples.jpa.entity.mapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vague_entity")
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vague_type")
public abstract class VagueEntity extends MappedEntity {

  @Column(name = "common_vague")
  private String commonVague;
}
