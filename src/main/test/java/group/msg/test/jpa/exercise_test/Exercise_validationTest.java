package group.msg.test.jpa.exercise_test;

import group.msg.examples.jpa.exercise_entityPackage.Address;
import group.msg.examples.jpa.exercise_entityPackage.Student;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

@RunWith(Arquillian.class)
public class Exercise_validationTest extends JPABaseTest {
    @PersistenceUnit(unitName = "test")
    private EntityManagerFactory emf;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testUpdate() throws Exception {
        validatePersistenceStudent(49, "street", "city", "country",
                "someone", "someone1", "firstLastName@gmail.com");
//        validatePersistenceStudent(49, "street", null, null,
//                null, "hello", "validation@gmail.com");
    }
    private void validatePersistenceStudent(int id, String street, String city, String country, String firstname,String lastname,
                                            String email) throws Exception {

        System.out.println("Validating student with first name: " + firstname);
        EntityManager entityManager = emf.createEntityManager();
        utx.begin();
        entityManager.joinTransaction();

        Student student = new Student();
        student.setLastName(lastname);
        student.setFirstName(firstname);
        student.setAddress(new Address(street, city, country));
        student.setEmail(email);

        try {
            entityManager.persist(student);
            utx.commit();
        } catch (ConstraintViolationException e) {
            utx.rollback();
            ConstraintViolation<?> constraint = e.getConstraintViolations().iterator().next();
            javax.validation.Path propertyPath = constraint.getPropertyPath();
            if (!Objects.equals(firstname, propertyPath.toString()) || !Objects.equals(lastname, propertyPath.toString()) || !Objects.equals(email, propertyPath.toString()) || !Objects.equals(street, propertyPath.toString()) || !Objects.equals(city, propertyPath.toString()) || !Objects.equals(country, propertyPath.toString())) {
                //System.out.println("Invalid constraint violation for field: " + propertyPath);
                 System.out.println(constraint.getMessage());

            }
        }
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
