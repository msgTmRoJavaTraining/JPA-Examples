package group.msg.test.jpa.entity;

import group.msg.examples.jpa.entity.mapping.*;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;

@RunWith(Arquillian.class)
public class MappingEntityCRUDTest extends JPABaseTest {

  @Test
  public void testCreateManyEntity() {
    ManyEntity manyEntity = new ManyEntity();
    em.persist(manyEntity);

    OneEntity oneEntity = new OneEntity();
    oneEntity.setMany(manyEntity);
    oneEntity.setOneToOne(manyEntity);
    em.persist(oneEntity);

    manyEntity.setOne(oneEntity);

    Query check = em.createNativeQuery("select me.id from many_entity me where me.ONE_ONE = " + oneEntity.getId());
    Assert.assertEquals("Entity not updated!", 1, check.getResultList().size());

    manyEntity.getManyToMany().add(oneEntity);

    check = em.createNativeQuery("select mm.many_id from many_many mm where mm.ONE_ID = " + oneEntity.getId());
    Assert.assertEquals("Entity not updated!", 1, check.getResultList().size());
  }

  @Test
  public void testCreateMappedEntity() {
    ConcreteEntity concreteEntity = new ConcreteEntity();
    concreteEntity.setConcrete("concrete_test");
    em.persist(concreteEntity);

    Query check = em.createNativeQuery("select ce.id from concrete_entity ce where ce.concrete = 'concrete_test'");
    Assert.assertEquals("Entity not updated!", 1, check.getResultList().size());

    SubVagueEntity subVagueEntity = new SubVagueEntity();
    subVagueEntity.setCommonVague("sub_common");
    subVagueEntity.setSubVague("very_sub_vague");
    em.persist(subVagueEntity);

    check = em.createNativeQuery("select ve.id from vague_entity ve where ve.vague_type = 'sub_vague_entity'");
    Assert.assertEquals("Entity not updated!", 1, check.getResultList().size());

    SuperVagueEntity superVagueEntity = new SuperVagueEntity();
    superVagueEntity.setCommonVague("super_common");
    superVagueEntity.setSuperVague("very_super_vague");
    em.persist(superVagueEntity);

    check = em.createNativeQuery("select ve.id from vague_entity ve where ve.vague_type = 'super_vague_entity'");
    Assert.assertEquals("Entity not updated!", 1, check.getResultList().size());
  }

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
            .addPackages(true, "group.msg")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
  }
}
