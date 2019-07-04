package group.msg.examples.jpa.entity.mapping;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MappedEntity {

  @Id
  @GeneratedValue
  private long id;

}
