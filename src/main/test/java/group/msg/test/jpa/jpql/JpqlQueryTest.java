package group.msg.test.jpa.jpql;

import group.msg.examples.jpa.entity.SimpleEntity;
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
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class JpqlQueryTest extends JPABaseTest {

  private static List<Object> persistedObjects = new ArrayList<>();

  @Test
  public void testSimpleJpql() {
    TypedQuery<ManyEntity> jpql = em.createQuery("select me from ManyEntity me where me.one.many = me", ManyEntity.class);
    Assert.assertEquals("Entity not found in the database!", 1, jpql.getResultList().size());

    Query path = em.createQuery("select oe.many.id from OneEntity oe");
    Assert.assertEquals("Query did not return the expected results!", 1, path.getResultList().size());

    TypedQuery<ConcreteEntity> constructor = em.createQuery("select new group.msg.examples.jpa.entity.EmbeddableEntity(se.name, se.id) " +
                    "from SimpleEntity se", ConcreteEntity.class);
    Assert.assertEquals("Query did not return the expected results!", 2, constructor.getResultList().size());

    TypedQuery<ManyEntity> join = em.createQuery("select me from OneEntity oe join oe.oneToOne me", ManyEntity.class);
    Assert.assertEquals("Query did not return the expected results!", 1, join.getResultList().size());

    TypedQuery<ManyEntity> fetch = em.createQuery("select me from OneEntity oe join fetch oe.many me", ManyEntity.class);
    Assert.assertEquals("Query did not return the expected results!", 1, fetch.getResultList().size());
  }

  @Test
  public void testWhereJpql() {
    TypedQuery<SimpleEntity> like = em.createQuery("select se from SimpleEntity se where se.name like '%bere'", SimpleEntity.class);
    SimpleEntity singleResult = like.getSingleResult();
    Assert.assertEquals("Query did not return the correct result!", "Pulbere", singleResult.getName());

    TypedQuery<SimpleEntity> param = em.createQuery("select se from SimpleEntity se where se.name like ?1", SimpleEntity.class);
    param.setParameter(1, "P%a_");
    singleResult = param.getSingleResult();
    Assert.assertEquals("Query did not return the correct result!", "Praf", singleResult.getName());

    TypedQuery<VagueEntity> escape = em.createQuery("select ve from VagueEntity ve where ve.commonVague like 'sub\\_%' escape '\\'",
            VagueEntity.class);
    VagueEntity result = escape.getSingleResult();
    Assert.assertEquals("Query did not return the correct result!", "sub_common", result.getCommonVague());
  }

  @Test
  public void testSubQueryJpql() {
    Query exists = em.createQuery("select me from ManyEntity me where exists (select 1 from OneEntity oe where oe.oneToOne = me)");
    Assert.assertEquals("Query did not return the correct result!", 1, exists.getResultList().size());

    Query in = em.createQuery("select ve from VagueEntity ve where ve.commonVague in ('sub_common', 'super_common')");
    Assert.assertEquals("Query did not return the correct result!", 2, in.getResultList().size());

    Query type = em.createQuery("select ve from VagueEntity ve where type(ve) = SubVagueEntity");
    Assert.assertEquals("Query did not return the correct result!", 1, type.getResultList().size());

    Query functions = em.createQuery("select upper(ve.commonVague) , current_timestamp, length(ve.commonVague), " +
            "locate('mm',ve.commonVague, 3) from VagueEntity ve order by ve.id desc");
    Object[] result = (Object[]) functions.getResultList().get(0);
    Assert.assertEquals("Query did not return the correct result!",result[0], "SUPER_COMMON");
    Assert.assertEquals("Query did not return the correct result!",result[2], 12);
    Assert.assertEquals("Query did not return the correct result!",result[3], 9);

    Query caseSelect = em.createQuery("select ve.commonVague, " +
            "case when type(ve) = SubVagueEntity then 'SUB' " +
            "     when type(ve) = SuperVagueEntity then 'SUPER' " +
            "     else 'UNKNOWN' " +
            "end " +
            "from VagueEntity ve order by ve.id desc");
    result = (Object[]) caseSelect.getResultList().get(0);
    Assert.assertEquals("Query did not return the correct result!",result[1], "SUPER");
    result = (Object[]) caseSelect.getResultList().get(1);
    Assert.assertEquals("Query did not return the correct result!",result[1], "SUB");
  }

  @Test
  public void testAggregateJpql() {
    Query groupBy = em.createQuery("select type(ve), avg(ve.id), sum(ve.id), min(ve.id) from VagueEntity ve group by type(ve)");
    Assert.assertEquals("Query did not return the correct result!", 2, groupBy.getResultList().size());

    Query having = em.createQuery("select type(ve), avg(ve.id), sum(ve.id), min(ve.id) from VagueEntity ve group by type(ve) " +
            "having sum(ve.id) > 0");
    Assert.assertEquals("Query did not return the correct result!", 2, having.getResultList().size());
  }

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
            .addPackages(true, "group.msg")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
  }

  @Override
  protected void insertData() throws Exception {
    utx.begin();
    em.joinTransaction();

    SimpleEntity praf = new SimpleEntity();
    praf.setName("Praf");
    em.persist(praf);
    persistedObjects.add(praf);

    SimpleEntity pulbere = new SimpleEntity();
    pulbere.setName("Pulbere");
    em.persist(pulbere);
    persistedObjects.add(pulbere);

    ManyEntity manyEntity = new ManyEntity();
    em.persist(manyEntity);

    OneEntity oneEntity = new OneEntity();
    oneEntity.setMany(manyEntity);
    oneEntity.setOneToOne(manyEntity);
    em.persist(oneEntity);

    manyEntity.setOne(oneEntity);
    em.flush();
    manyEntity.getManyToMany().add(oneEntity);

    ConcreteEntity concreteEntity = new ConcreteEntity();
    concreteEntity.setConcrete("concrete_test");
    em.persist(concreteEntity);
    persistedObjects.add(concreteEntity);

    SubVagueEntity subVagueEntity = new SubVagueEntity();
    subVagueEntity.setCommonVague("sub_common");
    subVagueEntity.setSubVague("very_sub_vague");
    em.persist(subVagueEntity);
    persistedObjects.add(subVagueEntity);

    SuperVagueEntity superVagueEntity = new SuperVagueEntity();
    superVagueEntity.setCommonVague("super_common");
    superVagueEntity.setSuperVague("very_super_vague");
    em.persist(superVagueEntity);
    persistedObjects.add(superVagueEntity);

    utx.commit();
  }

  @Override
  protected void internalClearData() {
    em.createNativeQuery("update one_entity set one_many = null").executeUpdate();
    em.createNativeQuery("update many_entity set one_one = null").executeUpdate();
    em.createNativeQuery("delete from many_many").executeUpdate();
    em.createNativeQuery("delete from one_entity").executeUpdate();
    em.createNativeQuery("delete from many_entity").executeUpdate();


    for(Object persisted : persistedObjects) {
      Object entity = em.merge(persisted);
      em.remove(entity);
    }
  }
}
