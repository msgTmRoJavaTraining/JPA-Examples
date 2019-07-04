package group.msg.examples.jpa.entity.primary_keys;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Project {

  @EmbeddedId
  private ProjectId id;

}
