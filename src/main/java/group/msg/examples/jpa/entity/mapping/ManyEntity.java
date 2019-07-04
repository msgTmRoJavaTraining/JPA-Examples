package group.msg.examples.jpa.entity.mapping;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "many_entity")
public class ManyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @OneToMany(mappedBy = "many")
  private Collection<OneEntity> oneToMany;

  @OneToOne
  @JoinColumn(name = "one_one")
  private OneEntity one;

  @ManyToMany
  @JoinTable(name = "many_many",
          joinColumns = @JoinColumn(name = "many_id"),
          inverseJoinColumns = @JoinColumn(name = "one_id"))
  private Collection<OneEntity> manyToMany;
}
