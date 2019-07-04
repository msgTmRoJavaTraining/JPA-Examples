package group.msg.examples.jpa.entity.mapping;

import lombok.Data;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.TimeOfDay;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Cacheable
@Cache(type = CacheType.FULL, expiryTimeOfDay = @TimeOfDay(hour = 18, minute = 30))
@Table(name = "one_entity")
public class OneEntity {

  @Id
  @GeneratedValue(generator = "Map_Seq")
  @SequenceGenerator(name = "Map_Seq", sequenceName = "Mapping_Sequence")
  private long id;

  @ManyToOne
  @JoinColumn(name = "one_many")
  private ManyEntity many;

  @OneToOne(mappedBy = "one")
  private ManyEntity oneToOne;

  @ManyToMany
  @JoinTable(name = "many_many",
          joinColumns = @JoinColumn(name = "one_id"),
          inverseJoinColumns = @JoinColumn(name = "many_id"))
  private Collection<ManyEntity> manyToMany;
}
