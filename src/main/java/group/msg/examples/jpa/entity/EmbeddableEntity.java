package group.msg.examples.jpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class EmbeddableEntity {


  @Column(name = "embed_1")
  private String embeddableString;

  private int embeddableInt;

  public EmbeddableEntity() {}

  public EmbeddableEntity(String embStr, int embInt) {
    embeddableString = embStr;
    embeddableInt = embInt;
  }
}
