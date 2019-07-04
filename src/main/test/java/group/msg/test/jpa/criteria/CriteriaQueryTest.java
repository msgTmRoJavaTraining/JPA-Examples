package group.msg.test.jpa.criteria;

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

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class CriteriaQueryTest extends JPABaseTest {

  private static List<Object> persistedObjects = new ArrayList<>();

  @Test
  public void testSimpleCriteria() {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<SimpleEntity> likeQuery = builder.createQuery(SimpleEntity.class);
    Root<SimpleEntity> likeEntity = likeQuery.from(SimpleEntity.class);
    CriteriaQuery<SimpleEntity> likeSelect = likeQuery.select(likeEntity);
    likeSelect.where(builder.like(likeEntity.get("name"), "%bere"));

    SimpleEntity singleResult = em.createQuery(likeQuery).getSingleResult();
    Assert.assertEquals("Query did not return the correct result!", "Pulbere", singleResult.getName());

    CriteriaQuery<SimpleEntity> parameterQuery = builder.createQuery(SimpleEntity.class);
    Root<SimpleEntity> parameterEntity = parameterQuery.from(SimpleEntity.class);
    CriteriaQuery<SimpleEntity> parameterSelect = parameterQuery.select(parameterEntity);
    ParameterExpression<String> parameterExpression = builder.parameter(String.class, "nameParam");
    parameterSelect.where(builder.like(parameterEntity.get("name"), parameterExpression));

    TypedQuery<SimpleEntity> query = em.createQuery(parameterQuery);
    query.setParameter("nameParam", "P%a_");
    singleResult = query.getSingleResult();
    Assert.assertEquals("Query did not return the correct result!", "Praf", singleResult.getName());

    CriteriaQuery<VagueEntity> escapeQuery = builder.createQuery(VagueEntity.class);
    Root<VagueEntity> escapeEntity = escapeQuery.from(VagueEntity.class);
    CriteriaQuery<VagueEntity> escapeSelect = escapeQuery.select(escapeEntity);
    escapeSelect.where(builder.like(escapeEntity.get("commonVague"), "sub_%"));

    VagueEntity vagueResult = em.createQuery(escapeQuery).getSingleResult();
    Assert.assertEquals("Query did not return the correct result!", "sub_common", vagueResult.getCommonVague());
  }

  @Test
  public void testSubQueryCriteria() {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<ManyEntity> existsQuery = builder.createQuery(ManyEntity.class);
    Root<ManyEntity> existsEntity = existsQuery.from(ManyEntity.class);

    Subquery<OneEntity> existsSubQuery = existsQuery.subquery(OneEntity.class);
    Root<OneEntity> existsSubEntity = existsQuery.from(OneEntity.class);
    existsSubQuery.select(existsSubEntity).where(builder.equal(existsSubEntity.get("oneToOne"), existsEntity));

    existsQuery.select(existsEntity).where(builder.exists(existsSubQuery));

    List<ManyEntity> queryResult = em.createQuery(existsQuery).getResultList();
    Assert.assertEquals("Query did not return the correct result!", 1, queryResult.size());

    CriteriaQuery<VagueEntity> inQuery = builder.createQuery(VagueEntity.class);
    Root<VagueEntity> inEntity = inQuery.from(VagueEntity.class);
    inQuery.select(inEntity).where(inEntity.get(VagueEntity_.commonVague).in("sub_common", "super_common"));

    List<VagueEntity> inResult = em.createQuery(inQuery).getResultList();
    Assert.assertEquals("Query did not return the correct result!", 2, inResult.size());

    CriteriaQuery<Object> orderQuery = builder.createQuery();
    EntityType<VagueEntity> vagueEntityType = em.getMetamodel().entity(VagueEntity.class);
    Root<VagueEntity> orderEntity = orderQuery.from(VagueEntity.class);
    Path<String> commonVague = orderEntity.get(vagueEntityType.getSingularAttribute("commonVague", String.class));
    orderQuery.multiselect(builder.upper(commonVague), builder.currentTimestamp(), builder.length(commonVague),
            builder.locate(commonVague, "mm", 3));
    orderQuery.orderBy(builder.desc(orderEntity.get("id")));
    Object[] result = (Object[]) em.createQuery(orderQuery).getResultList().get(0);
    Assert.assertEquals("Query did not return the correct result!",result[0], "SUPER_COMMON");
    Assert.assertEquals("Query did not return the correct result!",result[2], 12);
    Assert.assertEquals("Query did not return the correct result!",result[3], 9);

    CriteriaQuery<Object> caseQuery = builder.createQuery();
    Root<VagueEntity> caseEntity = caseQuery.from(VagueEntity.class);
    commonVague = caseEntity.get(vagueEntityType.getSingularAttribute("commonVague", String.class));
    caseQuery.multiselect(commonVague, builder.selectCase()
            .when(builder.equal(caseEntity.type(), SubVagueEntity.class),"SUB")
            .when(builder.equal(caseEntity.type(), SuperVagueEntity.class),"SUPER")
            .otherwise("UNKNOWN"));
    caseQuery.orderBy(builder.desc(caseEntity.get("id")));

    List<Object> resultList = em.createQuery(caseQuery).getResultList();
    result = (Object[]) resultList.get(0);
    Assert.assertEquals("Query did not return the correct result!",result[1], "SUPER");
    result = (Object[]) resultList.get(1);
    Assert.assertEquals("Query did not return the correct result!",result[1], "SUB");
  }

  @Test
  public void testAggregateCriteria() {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Object> groupQuery = builder.createQuery();
    Root<VagueEntity> groupEntity = groupQuery.from(VagueEntity.class);
    groupQuery.multiselect(groupEntity.type(), builder.avg(groupEntity.get("id")).alias("averageId"), builder.sum(groupEntity.get("id"))
                    .alias("sumId"), builder.min(groupEntity.get("id")).alias("minId"));
    groupQuery.groupBy(groupEntity.type());

    List<Object> result = em.createQuery(groupQuery).getResultList();
    Assert.assertEquals("Query did not return the correct result!", 2, result.size());

    CriteriaQuery<Object> havingQuery = builder.createQuery();
    Root<VagueEntity> havingEntity = havingQuery.from(VagueEntity.class);
    havingQuery.multiselect(havingEntity.type(), builder.avg(havingEntity.get("id")).alias("averageId"), builder.sum(havingEntity.get("id"))
            .alias("sumId"), builder.min(havingEntity.get("id")).alias("minId"));
    havingQuery.groupBy(havingEntity.type());
    havingQuery.having(builder.greaterThan(builder.sum(havingEntity.get("id")), 0));

    result = em.createQuery(havingQuery).getResultList();
    Assert.assertEquals("Query did not return the correct result!", 2, result.size());
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

    SimpleEntity pulbere = new SimpleEntity();
    pulbere.setName("Pulbere");
    em.persist(pulbere);
    persistedObjects.add(pulbere);

    SimpleEntity praf = new SimpleEntity();
    praf.setName("Praf");
    em.persist(praf);
    persistedObjects.add(praf);

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
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaUpdate<ManyEntity> manyUpdate = builder.createCriteriaUpdate(ManyEntity.class);
    Root<ManyEntity> manyUpdateRoot = manyUpdate.from(ManyEntity.class);
    manyUpdate.set(manyUpdateRoot.get("one"), (Object) null);
    em.createQuery(manyUpdate).executeUpdate();

    CriteriaUpdate<OneEntity> oneUpdate = builder.createCriteriaUpdate(OneEntity.class);
    Root<OneEntity> oneUpdateRoot = oneUpdate.from(OneEntity.class);
    oneUpdate.set(oneUpdateRoot.get("many"), (Object) null);
    em.createQuery(oneUpdate).executeUpdate();

    CriteriaDelete<ManyEntity> manyDelete = builder.createCriteriaDelete(ManyEntity.class);
    em.createQuery(manyDelete).executeUpdate();
    CriteriaDelete<OneEntity> oneDelete = builder.createCriteriaDelete(OneEntity.class);
    em.createQuery(oneDelete).executeUpdate();

    for(Object persisted : persistedObjects) {
      Object entity = em.merge(persisted);
      em.remove(entity);
    }
  }
}
