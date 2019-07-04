package group.msg.examples.jpa.entity.mapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "concrete_entity")
public class ConcreteEntity extends MappedEntity {

  private String concrete;

  public ConcreteEntity() {}

  public ConcreteEntity(String concrete) {
    this.concrete = concrete;
  }
}
