package group.msg.test.jpa.schema;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@RunWith(Arquillian.class)
public class SchemaGenerationTest {

  @PersistenceContext(unitName = "test")
  private EntityManager em;

  @Test
  public void testSchemaGeneration() {
    Query check = em.createNativeQuery("select ce.id from create_example ce where ce.id = 999");
    Assert.assertEquals("Entity not found!", 1, check.getResultList().size());
  }

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
            .addPackages(true, "group.msg")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsResource("../classes/META-INF/sql/drop_script.sql", "META-INF/sql/drop_script.sql")
            .addAsResource("../classes/META-INF/sql/create_script.sql", "META-INF/sql/create_script.sql")
            .addAsResource("../classes/META-INF/sql/insert_data.sql", "META-INF/sql/insert_data.sql")
            .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
  }
}
