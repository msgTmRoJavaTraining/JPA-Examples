package group.msg.test.jpa;

import group.msg.examples.jpa.entity.EmbeddableAddressEntity;
import group.msg.examples.jpa.entity.StudentEntity;
import group.msg.examples.jpa.entity.UniversityEntity;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class FirstExerciseTest extends JPABaseTest {
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    @Inject
    Logger logger;

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
        List<StudentEntity> mockStudents = new ArrayList<>();

        UniversityEntity univ = new UniversityEntity();
        univ.setName("UPT");
        univ.setCountry("Romania");

        StudentEntity stud = new StudentEntity();
        stud.setFirst_name("Popescu");
        stud.setLast_name("Ion");

        EmbeddableAddressEntity address = new EmbeddableAddressEntity();
        address.setCountry("Romania");
        address.setCity("Timisoara");
        address.setStreet("Torontalului");
        stud.setAddress(address);
        stud.setUniversity(univ);


        univ.setStudents(mockStudents);

        em.persist(stud);
        em.persist(univ);
        utx.commit();

        em.clear();
    }

    @Test
    public void firstTest() {
        Query q = em.createNativeQuery("select * from Universities");
        Assert.assertEquals("Entity not updated!", 1, q.getResultList().size());
    }

    @Test
    public void breakingRulesTest() throws SystemException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        UniversityEntity noCountryUniversity = new UniversityEntity();
        noCountryUniversity.setName("UVT");

        Set<ConstraintViolation<UniversityEntity>> violations = validator.validate(noCountryUniversity);

        for (ConstraintViolation<UniversityEntity> violation : violations) {
            logger.severe("Error: " + violation.getMessage());
        }
        em.persist(noCountryUniversity);
        utx.commit();
        em.clear();
    }

    @Test
    public void AddStudentFromAnotherCityTest() throws SystemException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        UniversityEntity university = new UniversityEntity();
        university.setName("Agronomie");
        university.setCountry("Romania");

        EmbeddableAddressEntity address = new EmbeddableAddressEntity();
        address.setCountry("Poland");
        address.setCity("Warsaw");
        address.setStreet("Streeeeeet");

        StudentEntity student = new StudentEntity();
        student.setFirst_name("Popescu");
        student.setLast_name("Ion");
        student.setUniversity(university);
        student.setAddress(address);

        Set<ConstraintViolation<StudentEntity>> violations = validator.validate(student);

        for (ConstraintViolation<StudentEntity> violation : violations) {
            logger.severe("Error: " + violation.getMessage());
        }
        em.persist(university);
        em.persist(student);
        utx.commit();
        em.clear();
    }

    @Test
    public void AddBannedStudentTest() throws SystemException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        StudentEntity bannedStudent = new StudentEntity();
        bannedStudent.setFirst_name("Stifler");
        bannedStudent.setLast_name("Ion");

        UniversityEntity university = new UniversityEntity();
        university.setName("BEST ONE");
        university.setCountry("Romania");
        bannedStudent.setUniversity(university);

        EmbeddableAddressEntity address = new EmbeddableAddressEntity();
        address.setCountry("Romania");
        address.setCity("Bucuresti");
        address.setStreet("Recunostintei");

        bannedStudent.setAddress(address);

        Set<ConstraintViolation<StudentEntity>> violations = validator.validate(bannedStudent);

        for (ConstraintViolation<StudentEntity> violation : violations) {
            logger.severe("Error: " + violation.getMessage());
        }
        em.persist(bannedStudent);
        em.persist(address);
        utx.commit();
        em.clear();
    }

    @Override
    protected void internalClearData() {
        em.createNativeQuery("delete from Students").executeUpdate();
        em.createNativeQuery("delete from Universities").executeUpdate();
        em.createNativeQuery("delete from STUDENT_SUBJECT").executeUpdate();
    }
}
