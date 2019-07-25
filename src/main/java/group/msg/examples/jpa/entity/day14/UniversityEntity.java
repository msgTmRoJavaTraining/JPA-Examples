package group.msg.examples.jpa.entity.day14;


import group.msg.examples.jpa.entity.SimpleType;
import group.msg.examples.jpa.entity.mapping.OneEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Entity
@Table(name = "university_entity")
public class UniversityEntity {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "university_id")
    private Collection<StudentEntity> oneToMany;

    @Version
    private long version;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "country")
    private String country;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

}
