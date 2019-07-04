package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "simple_entity")
@NamedQueries({
        @NamedQuery(name = "JPAExample.findAll",
                query = "select e from SimpleEntity e order by e.id desc"),
        @NamedQuery(name = "JPAExample.findById",
                query = "select e from SimpleEntity e where e.id = :id")
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "JPAExample.findIdByName",
                query = "select sp.id from simple_entity sp where sp.user_name like ?1")
})
public class SimpleEntity implements Serializable {

  @Id
  @GeneratedValue
  private int id;

  @Version
  private long version;

  @Column(name = "user_name")
  private String name;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] picture;

  @Enumerated(EnumType.STRING)
  private SimpleType type;

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name="embeddableInt", column = @Column(name = "embed_2"))
  })
  private EmbeddableEntity embeddable;
}
