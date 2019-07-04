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

import javax.persistence.*;

@RunWith(Arquillian.class)
public class EntityLockingAndCachingTest extends JPABaseTest {

  private static final int NUMBER_OF_ENTITIES = 5;

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
            .addPackages(true,"group.msg")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
  }

  @Test
  public void testEntityManagerLocking() {
    System.out.println("Locking entities...");
    SimpleEntity thirdEntity = em.find(SimpleEntity.class, 6);
    em.lock(thirdEntity, LockModeType.PESSIMISTIC_READ);

    SimpleEntity secondEntity = em.find(SimpleEntity.class, 7);
    em.lock(secondEntity, LockModeType.OPTIMISTIC);

    SimpleEntity firstEntity = em.find(SimpleEntity.class, 8, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
    Assert.assertEquals(firstEntity.getVersion(), 1);
    firstEntity.setName("Force increment");
    em.flush();
    Assert.assertEquals(firstEntity.getVersion(), 2);
  }

  @Test
  public void testQueryLocking() {
    TypedQuery<SimpleEntity> query = em.createNamedQuery("JPAExample.findById", SimpleEntity.class);
    query.setParameter("id", 1);
    query.setLockMode(LockModeType.PESSIMISTIC_READ);
    query.setHint("javax.persistence.lock.timeout", 60000);
    query.setHint("javax.persistence.lock.scope", PessimisticLockScope.EXTENDED);
    SimpleEntity firstEntity = query.getSingleResult();

    em.lock(firstEntity, LockModeType.NONE);
  }

  @Test
  public void testEntityCache() {
    JpaCache cache = (JpaCache) em.getEntityManagerFactory().getCache();
    Object cachedEntity = cache.getObject(SimpleEntity.class, 1);
    Assert.assertNotNull(cachedEntity);

    SimpleEntity deletedEntity = em.find(SimpleEntity.class, 1);
    Assert.assertNull(deletedEntity);

    Assert.assertFalse(cache.isValid(cachedEntity));

    cache.evict(cachedEntity);

    cachedEntity = cache.getObject(SimpleEntity.class, 1);
    Assert.assertNotNull(cachedEntity);

    cache.removeObject(cachedEntity);

    cachedEntity = cache.getObject(SimpleEntity.class, 1);
    Assert.assertNull(cachedEntity);
  }

  @Override
  protected void insertData() throws Exception {
    utx.begin();
    em.joinTransaction();
    System.out.println("Inserting records...");
    for (int i = 1; i <= NUMBER_OF_ENTITIES; i++) {
      SimpleEntity e = new SimpleEntity();
      if (i % 2 == 0) {
        e.setName("Locked");
      } else {
        e.setName("Unlocked");
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
