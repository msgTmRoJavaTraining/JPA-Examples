package group.msg.test.jpa.validator;

import group.msg.examples.jpa.validator.CustomValidation;
import group.msg.examples.jpa.validator.CustomValidator;
import group.msg.examples.jpa.validator.ValidationEntity;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@RunWith(Arquillian.class)
public class ValidationEntityTest extends JPABaseTest {

  @PersistenceUnit(unitName = "test")
  private EntityManagerFactory emf;

  @Test
  public void testValidationEntity() throws Exception{
    validatePersistenceObject("Normal validation", "validation@gmail.com", 1, null,
            null, 0, 0, "none");

    validatePersistenceObject(null, "validation@gmail.com", 1, null,
            null, 0, 0, "name");

    validatePersistenceObject("Email validation", "validation.com", 1, null,
            null, 0, 0, "email");

    validatePersistenceObject("Salary validation", "validation@gmail.com", 0, null,
            null, 0, 0, "salary");

    Calendar currentDate = Calendar.getInstance();
    currentDate.add(Calendar.MONTH, 1);
    validatePersistenceObject("Birthday validation", "validation@gmail.com", 1, currentDate.getTime(),
            null, 0, 0, "birthday");

    currentDate = Calendar.getInstance();
    currentDate.add(Calendar.MONTH, -1);
    validatePersistenceObject("EndDate validation", "validation@gmail.com", 1, null,
            currentDate.getTime(), 0, 0, "endDate");

    validatePersistenceObject("NoHardCoded valid", "validation@gmail.com", 1, null,
            null, 123, 0, "noHardCodedValidation");

    validatePersistenceObject("HardCoded valid", "validation@gmail.com", 1, null,
            null, 0, 123, "none");

    Query check = em.createNativeQuery("select ve.id from validation_entity ve");
    Assert.assertEquals("Validations failed!", 2, check.getResultList().size());
  }

  private void validatePersistenceObject(String name, String email, int salary, Date birthday, Date endDate, int noHardCodedValidation, int
          hardCodedValidation, String validationField) throws Exception {

    System.out.println("Validating entity with name: " + name);
    EntityManager entityManager = emf.createEntityManager();
    utx.begin();
    entityManager.joinTransaction();

    ValidationEntity entity = new ValidationEntity();
    entity.setName(name);
    entity.setEmail(email);
    entity.setSalary(salary);
    entity.setBirthday(birthday);
    entity.setEndDate(endDate);
    entity.setNoHardCodedValidation(noHardCodedValidation);
    entity.setHardCodedValidation(hardCodedValidation);

    try {
      entityManager.persist(entity);
      utx.commit();
    } catch (ConstraintViolationException e) {
      utx.rollback();
      ConstraintViolation<?> constraint = e.getConstraintViolations().iterator().next();
      javax.validation.Path propertyPath = constraint.getPropertyPath();
      if (!Objects.equals(validationField, propertyPath.toString())) {
        throw new IllegalArgumentException("Invalid constraint violation for field: " + propertyPath);
      }

    }
  }

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
            .addClass(ValidationEntity.class).addClass(CustomValidation.class).addClass(CustomValidator.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @Override
  protected void startTransaction() {
    // Multiple transactions needed
  }

  @Override
  public void commitTransaction() {
    // Don't commit non existent transaction
  }
}
