package group.msg.test.jpa.exerciseTests;

import group.msg.examples.jpa.exerciseEntityPackage.Address;
import group.msg.examples.jpa.exerciseEntityPackage.Student;
import group.msg.examples.jpa.exerciseEntityPackage.Subject;
import group.msg.examples.jpa.exerciseEntityPackage.University;
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
public class ExerciseValidationTests extends JPABaseTest {
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
        University university1 = new University();
        University university2 = new University();
        List<Subject> subjectsList1 = new ArrayList<>();
        List<Subject> subjectsList2 = new ArrayList<>();
        Subject subject1 = new Subject();
        Subject subject2 = new Subject();
        Subject subject3 = new Subject();
        Address address= null;
        subject1.setName("sub1");
        subject1.setSubject_id(1);

        subject2.setName("subjectsList2");
        subject2.setSubject_id(2);

        subject3.setName("sub3");
        subject3.setSubject_id(3);

        subjectsList1.add(subject1);
        subjectsList1.add(subject2);

        subjectsList2.add(subject3);
        subjectsList2.add(subject1);

        university1.setName("UVT");
        university1.setUniversity_id(12);
        university1.setCountry("country");

        university2.setName("UPT");
        university2.setUniversity_id(13);
        university2.setCountry("OtherCountry");

        validateEntry(49, address,"hello", "hi", "firstLastName@gmail.com",university1,subjectsList2);
        validateEntry(49, address,"someone", "hi", "firstLastName@gmail.com",university1,subjectsList2);
    }

    @Test
    public void testValidationForCountry() throws Exception {
        University uni1 = new University();
        University uni2 = new University();
        List<Subject> subjectsList1 = new ArrayList<>();
        List<Subject> subjectsList2 = new ArrayList<>();
        Subject subject1 = new Subject();
        Subject subject2 = new Subject();
        Subject subject3 = new Subject();
        Address address= new Address("somewhere","city", "Othercountry");
        subject1.setName("sub1");
        subject1.setSubject_id(1);

        subject2.setName("subjectsList2");
        subject2.setSubject_id(2);

        subject3.setName("sub3");
        subject3.setSubject_id(3);

        subjectsList1.add(subject1);
        subjectsList1.add(subject2);

        subjectsList2.add(subject3);
        subjectsList2.add(subject1);

        uni1.setName("UVT");
        uni1.setUniversity_id(12);
        uni1.setCountry("country");

        uni2.setName("UPT");
        uni2.setUniversity_id(13);
        uni2.setCountry("OtherCountry");

        validateEntry(49,address,"hello", "hi", "firstLastName@gmail.com",uni2,subjectsList1);
        validateEntry(49, address,"test", null, "validate",uni1,subjectsList2);
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
    }

    @Override
    public void commitTransaction() {
    }
}
