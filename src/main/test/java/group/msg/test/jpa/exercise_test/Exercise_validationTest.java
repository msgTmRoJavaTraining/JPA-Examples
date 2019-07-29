package group.msg.test.jpa.exercise_test;

import com.oracle.wls.shaded.org.apache.xalan.xsltc.dom.AdaptiveResultTreeImpl;
import group.msg.examples.jpa.exercise_entityPackage.Address;
import group.msg.examples.jpa.exercise_entityPackage.Student;
import group.msg.examples.jpa.exercise_entityPackage.Subject;
import group.msg.examples.jpa.exercise_entityPackage.University;
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
import java.util.ArrayList;
import java.util.List;
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
    public void testValidationForFields() throws Exception {
        University u1 = new University();
        University u2 = new University();
        List<Subject> sub = new ArrayList<>();
        List<Subject> sub2 = new ArrayList<>();
        Subject s1 = new Subject();
        Subject s2 = new Subject();
        Subject s3 = new Subject();
        Address adr2= null;
        s1.setName("sub1");
        s1.setSubject_id(1);
        s2.setName("sub2");
        s2.setSubject_id(2);
        s3.setName("sub3");
        s3.setSubject_id(3);

        sub.add(s1);
        sub.add(s2);
        sub2.add(s3);
        sub2.add(s1);
        u1.setName("UVT");
        u1.setUniversity_id(12);
        u1.setCountry("country");
        u2.setName("UPT");
        u2.setUniversity_id(13);
        u2.setCountry("OtherCountry");
        validateEntry(49, adr2,"hello", "hi", "firstLastName@gmail.com",u1,sub2);
        validateEntry(49, adr2,"someone", "hi", "firstLastName@gmail.com",u1,sub2);
    }

    @Test
    public void testValidationForCountry() throws Exception {
        University u1 = new University();
        University u2 = new University();
        List<Subject> sub = new ArrayList<>();
        List<Subject> sub2 = new ArrayList<>();
        Subject s1 = new Subject();
        Subject s2 = new Subject();
        Subject s3 = new Subject();
        Address adr= new Address("somewhere","city", "Othercountry");
        Address adr2=new Address("somewhere","city", "country1");
        s1.setName("sub1");
        s1.setSubject_id(1);
        s2.setName("sub2");
        s2.setSubject_id(2);
        s3.setName("sub3");
        s3.setSubject_id(3);

        sub.add(s1);
        sub.add(s2);
        sub2.add(s3);
        sub2.add(s1);

        u1.setName("UVT");
        u1.setUniversity_id(12);
        u1.setCountry("country");
        u2.setName("UPT");
        u2.setUniversity_id(13);
        u2.setCountry("OtherCountry");
        validateEntry(49,adr,"hello", "hi", "firstLastName@gmail.com",u2,sub);
        validateEntry(49, adr,"test", null, "validate",u1,sub2);
    }

    private void validateEntry(int id, Address adr, String firstname, String lastname,
                               String email, University uni, List<Subject> subjectList) throws Exception {

        System.out.println("Validating student with first name: " + firstname);
        EntityManager entityManager = emf.createEntityManager();
        utx.begin();
        entityManager.joinTransaction();

        Student student = new Student();
        student.setLastName(lastname);
        student.setFirstName(firstname);
        student.setAddress(adr);
        student.setEmail(email);
        student.setUniversity(uni);
        student.setSubjects(subjectList);

        try {
            entityManager.persist(student);
            utx.commit();
        } catch (ConstraintViolationException e) {
            utx.rollback();
            ConstraintViolation<?> constraint = e.getConstraintViolations().iterator().next();
            javax.validation.Path propertyPath = constraint.getPropertyPath();
            if (!Objects.equals(firstname, propertyPath.toString()) || !Objects.equals(lastname, propertyPath.toString()) ||  !Objects.equals(adr.getStreet(), propertyPath.toString()) || !Objects.equals(adr.getCity(), propertyPath.toString()) || !Objects.equals(adr.getCountry(), propertyPath.toString())) {
                System.out.println("Invalid constraint violation for field: " + propertyPath);
                System.out.println(constraint.getMessage());

            }
        }catch (Exception e){
            e.printStackTrace();
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
