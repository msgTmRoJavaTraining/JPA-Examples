package group.msg.test.jpa.ExerciseTest;

import group.msg.exercises.entities.*;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.SystemException;

import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static group.msg.exercises.entities.Grades_.student;


@RunWith(Arquillian.class)
public class JpaFirstDayEexerciseTest extends JPABaseTest {

    @Inject
    Logger logger;

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }


    @Test
    public void studentCityTest() throws SystemException, HeuristicRollbackException, HeuristicMixedException, RollbackException, javax.transaction.SystemException {

        Student stud = new Student();
        stud.setFirst_name("Gh");
        stud.setLast_name("Ion");

        University university = new University();
        university.setName("Upt");
        university.setCountry("Romania");
        stud.setUniversity_id(university);

        Adress address = new Adress();
        address.setCountry("Romania");
        address.setCity("Timisoara");
        address.setStreet("Princip");

        stud.setAdress(address);


        Set<ConstraintViolation<Student>> restricted = validator.validate(stud);

        for (ConstraintViolation<Student> violation : restricted) {
            logger.warning("Error: " + violation.getMessage());
        }
        em.persist(university);
        em.persist(student);
        utx.commit();
        em.clear();
    }

    @Test
    public void testBannedStud() throws SystemException, HeuristicRollbackException, HeuristicMixedException, RollbackException, javax.transaction.SystemException {
        Student stud = new Student();
        stud.setFirst_name("Gh");
        stud.setLast_name("Ion");

        University university = new University();
        university.setName("Upt");
        university.setCountry("Romania");
        stud.setUniversity_id(university);

        Adress address = new Adress();
        address.setCountry("Romania");
        address.setCity("Timisoara");
        address.setStreet("Princip");

        stud.setAdress(address);

        Set<ConstraintViolation<Student>> restricted = validator.validate(stud);

        for (ConstraintViolation<Student> violation : restricted) {
            logger.warning("Error: " + violation.getMessage());
        }
        em.persist(stud);
        em.persist(address);
        utx.commit();
        em.clear();
    }


    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();

        List<Student> studList = new ArrayList<>();
        List<Grades> gradeList = new ArrayList<>();

        Student s1 = new Student();
        Student s2 = new Student();

        Subject sub1 = new Subject();
        Subject sub2 = new Subject();

        Adress a1 = new Adress();
        Adress a2 = new Adress();

        Grades g1 = new Grades();
        Grades g2 = new Grades();

        g1.setValue(5);
        g2.setValue(9);

        gradeList.add(g1);
        gradeList.add(g2);

        a1.setCountry("Romania");
        a1.setCity("Timisoara");
        a1.setStreet("Princ");

        a2.setCity("Buc");
        a2.setStreet("Sec");
        a2.setCountry("Rom");

        s1.setFirst_name("Ion");
        s1.setLast_name("Gh");
        s1.setAdress(a1);
        s1.setGrades(gradeList);

        s2.setLast_name("In");
        s2.setFirst_name("Gheorghe");
        s2.setAdress(a2);
        s2.setGrades(gradeList);


        studList.add(s1);
        studList.add(s2);

        sub1.setName("Math");
        sub1.setManyToManyStudent(studList);
        sub1.setGrade(g1);

        sub2.setName("English");
        sub2.setManyToManyStudent(studList);
        sub2.setGrade(g2);

        for (Grades grad : gradeList)
            em.persist(grad);

        for (Student stud : studList)
            em.persist(stud);

        em.persist(sub1);
        em.persist(sub2);

        utx.commit();
    }

    @Test
    public void selectStudent() {

    }

    @Test
    public void deleteStudent() {

    }

    @Override
    protected void internalClearData() {

        em.createQuery("delete from Student").executeUpdate();
        em.createQuery("delete from Subject").executeUpdate();
        em.createQuery("delete from Grades").executeUpdate();


    }

}
