package group.msg.examples.jpa.entity.primary_keys;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ProjectId implements Serializable {

  private int departmentId;

  private long projectId;
}
