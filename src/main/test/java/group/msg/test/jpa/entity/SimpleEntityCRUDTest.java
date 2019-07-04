package group.msg.test.jpa.entity;

import group.msg.examples.jpa.entity.SimpleEntity;
import group.msg.examples.jpa.entity.SimpleType;
import group.msg.test.jpa.JPABaseTest;
import org.eclipse.persistence.jpa.JpaCache;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Cache;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@RunWith(Arquillian.class)
public class SimpleEntityCRUDTest extends JPABaseTest {

  private static final int NUMBER_OF_ENTITIES = 5;

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
            .addPackages(true,"group.msg")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
  }

  @Test
  public void testCreateSimpleEntity() {
    System.out.println("Checking number of created entities...");

    Query q = em.createNamedQuery("JPAExample.findAll");
    Assert.assertEquals("Entities not found in the database!", NUMBER_OF_ENTITIES, q.getResultList().size());
  }

  @Test
  public void testUpdateSimpleEntity() {
    TypedQuery<Integer> q = em.createNamedQuery("JPAExample.findIdByName", Integer.class);
    q.setParameter(1, "Pulbere");
    q.setMaxResults(1);
    Integer simpleEntityId = q.getSingleResult();

    SimpleEntity simpleEntity = em.find(SimpleEntity.class, simpleEntityId);
    simpleEntity.setType(SimpleType.SPECIAL);

    Query check = em.createNativeQuery("select sp.id from simple_entity sp where sp.type = 'SPECIAL'");
    Assert.assertEquals("Entity not updated!", 1, check.getResultList().size());
 }

  @Test
  public void testDeleteSimpleEntity() {
    TypedQuery<Integer> q = em.createNamedQuery("JPAExample.findIdByName", Integer.class);
    q.setParameter(1, "Praf");
    q.setMaxResults(1);
    Integer simpleEntityId = q.getSingleResult();

    SimpleEntity simpleEntity = em.find(SimpleEntity.class, simpleEntityId);
    em.remove(simpleEntity);

    Query check = em.createNamedQuery("JPAExample.findAll");
    Assert.assertEquals("Entities not found in the database!", NUMBER_OF_ENTITIES - 1, check.getResultList().size());
  }

  @Override
  protected void insertData() throws Exception {
    utx.begin();
    em.joinTransaction();
    System.out.println("Inserting records...");
    for (int i = 1; i <= NUMBER_OF_ENTITIES; i++) {
      SimpleEntity e = new SimpleEntity();
      if (i % 2 == 0) {
        e.setName("Praf");
      } else {
        e.setName("Pulbere");
      }
      em.persist(e);
    }
    utx.commit();

    em.clear();
  }

  @Override
  protected void internalClearData() {
    em.createQuery("delete from SimpleEntity").executeUpdate();
  }
}
