package group.msg.test.jpa.day14;

import group.msg.examples.jpa.entity.day14.AddressEntity;
import group.msg.examples.jpa.entity.day14.StudentEntity;
import group.msg.examples.jpa.entity.day14.SubjectEntity;
import group.msg.examples.jpa.entity.day14.UniversityEntity;
import group.msg.test.jpa.JPABaseTest;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class StudentCRUDTest extends JPABaseTest {
    @Inject
    private Logger logger;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true,"group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void createStudentyEntityTest() {
        System.out.println("Checking students...");

        Query q = em.createNativeQuery("select * from student_entity");
        Assert.assertEquals("Status students", 2, q.getResultList().size());
    }

    @Test
    public void findStudentyEntityTest() {
        System.out.println("Looking for Andrei in database...");

        Query q = em.createNativeQuery("select * from student_entity where FIRST_NAME = 'Andrei'");
        Assert.assertEquals("Status find", 1, q.getResultList().size());
    }

    @Test
    public void deleteStudentyEntityTest() throws SystemException, NotSupportedException {
        System.out.println("Deleting Caravan Mihai from database...");

        utx.begin();
        em.joinTransaction();
        StudentEntity studentEntity = em.find(StudentEntity.class, 51);
        em.remove(studentEntity);
    }

    @Test
    public void customStudentUniversityCountryValidation() throws SystemException, NotSupportedException {
        System.out.println("Running Second Validation Test");

        UniversityEntity university = new UniversityEntity();
        university.setName("Universitatea Politehnica Timisoara");
        university.setCountry("Coralium");

        AddressEntity address = new AddressEntity();
        address.setCountry("Romania");
        address.setCity("Timisoara");
        address.setStreet("Aleea Studentilor Caminul 14C");

        SubjectEntity subject = new SubjectEntity();
        subject.setName("Inteligenta Artificiala In Impas");

        StudentEntity student = new StudentEntity();
        student.setFirst_name("Bianca");
        student.setLast_name("Hisigan");
        student.setHome_address(address);
        student.setUniversity_id(university);
        student.setSection("Informatica");
        student.setEmail("biancahisigan@gmail.com");

        subject.setManyToMany(Collections.singletonList(student));

        utx.begin();
        em.joinTransaction();

        try {
            em.persist(university);
            em.persist(student);
            em.persist(subject);

            utx.commit();
        } catch (ConstraintViolationException e) {
            utx.rollback();
            ConstraintViolation<?> constraint = e.getConstraintViolations().iterator().next();
            String nameValidationMessage = constraint.getMessage();

            logger.info("Mesajul de eroare la validarea tarii este: " + nameValidationMessage);
        } catch (HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void customStudentNameValidation() throws SystemException, NotSupportedException {
        System.out.println("Running First Validation Test");

        UniversityEntity university = new UniversityEntity();
        university.setName("Universitatea de Vest");
        university.setCountry("Romania");

        AddressEntity address = new AddressEntity();
        address.setCountry("Romania");
        address.setCity("Timisoara");
        address.setStreet("Aleea Studentilor Caminul 11C");

        SubjectEntity subject = new SubjectEntity();
        subject.setName("Fizica Cuantica");

        StudentEntity student = new StudentEntity();
        student.setFirst_name("Bogdan^*1");
        student.setLast_name("Marasan");
        student.setHome_address(address);
        student.setUniversity_id(university);
        student.setSection("Informatica");
        student.setEmail("bogdanmarasan@gmail.com");

        subject.setManyToMany(Collections.singletonList(student));

        utx.begin();
        em.joinTransaction();

        try {
            em.persist(university);
            em.persist(student);
            em.persist(subject);

            utx.commit();
        } catch (ConstraintViolationException e) {
            utx.rollback();
            ConstraintViolation<?> constraint = e.getConstraintViolations().iterator().next();
            String nameValidationMessage = constraint.getMessage();

            logger.info("Mesajul de eroare la validarea numelui este: " + nameValidationMessage);
        } catch (HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");

        // *** Universitatea ***
        UniversityEntity university = new UniversityEntity();
        university.setName("Universitatea Politehnica Timisoara");
        university.setCountry("Romania");

        // *** Lista Adrese ***
        List<AddressEntity> addresses = new ArrayList<>();
        addresses.add(new AddressEntity());
        addresses.add(new AddressEntity());

        addresses.get(0).setCity("Timisoara");
        addresses.get(0).setStreet("Aleea Studentilor Camin 11C");
        addresses.get(0).setCountry("Romania");

        addresses.get(1).setCity("Timisoara");
        addresses.get(1).setStreet("Aleea Studentilor Camin 14C");
        addresses.get(1).setCountry("Romania");

        // *** Lista Studenti ***
        List<StudentEntity> students = new ArrayList<>();
        students.add(new StudentEntity());
        students.add(new StudentEntity());

        students.get(0).setStudent_id(50);
        students.get(0).setFirst_name("Andrei");
        students.get(0).setLast_name("Astanei");
        students.get(0).setHome_address(addresses.get(0));
        students.get(0).setUniversity_id(university);
        students.get(0).setSection("Informatica");
        students.get(0).setEmail("andreihao@gmail.com");

        students.get(1).setStudent_id(51);
        students.get(1).setFirst_name("Mihai");
        students.get(1).setLast_name("Caravan");
        students.get(1).setHome_address(addresses.get(1));
        students.get(1).setUniversity_id(university);
        students.get(1).setSection("Ingineria Sistemelor");
        students.get(1).setEmail("mihaicaravan@yahoo.com");

        // *** Lista Materii ***
        List<SubjectEntity> subjects = new ArrayList<>();
        subjects.add(new SubjectEntity());
        subjects.add(new SubjectEntity());
        subjects.get(0).setName("Programare Java");
        subjects.get(1).setName("Programarea Aplicatiilor Multimedia");

        subjects.get(0).setManyToMany(students);



        try {
            em.persist(university);

            for(SubjectEntity subjectEntity : subjects) {
                em.persist(subjectEntity);
            }

            for(StudentEntity s : students) {
                em.persist(s);
            }

            utx.commit();
        } catch (ConstraintViolationException e) {
            utx.rollback();
            ConstraintViolation<?> constraint = e.getConstraintViolations().iterator().next();
            String nameValidationMessage = constraint.getMessage();

            logger.info("Mesajul de eroare la validarea numelui este: " + nameValidationMessage);
        } catch (HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            e.printStackTrace();
        }

        em.clear();
    }

    @Override
    protected void internalClearData() {
        em.createQuery("delete from StudentEntity").executeUpdate();
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
