package group.msg.test.jpa;

import group.msg.examples.jpa.entity.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class SecondExerciseTest extends JPABaseTest {

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
    protected void internalClearData() {
        em.createQuery("delete from TruckEntity ").executeUpdate();
        em.createQuery("delete from CarEntity ").executeUpdate();
        em.createQuery("delete from BikeEntity ").executeUpdate();
        em.createQuery("delete from VehicleEntity ").executeUpdate();
    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setManufacturer("Mankind");

        BikeEntity bike = new BikeEntity();
        bike.setManufacturer("DHS");
        bike.setNoOfPassengers(1);
        bike.setSaddleHeight(12);

        CarEntity car = new CarEntity();
        car.setManufacturer("BMW");
        car.setNoOfDoors(4);
        car.setNoOfPassengers(5);

        TruckEntity truck = new TruckEntity();
        truck.setManufacturer("MAN");
        truck.setLoadCapacity(10000);
        truck.setNoOfContainers(100);

        em.persist(vehicle);
        em.persist(bike);
        em.persist(car);
        em.persist(truck);
        utx.commit();
        em.clear();
    }

    @Test
    public void gradeTesting() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException {
        List<GradeEntity> grades = new ArrayList<>();
        List<SubjectEntity> subjects = new ArrayList<>();

        SubjectEntity Math = new SubjectEntity();
        Math.setName("Math");
        SubjectEntity English = new SubjectEntity();
        Math.setName("English");
        subjects.add(English);

        StudentEntity student = new StudentEntity();
        student.setFirst_name("Greuceanu");
        student.setLast_name("Ion");
        GradeEntity grade1 = new GradeEntity();
        grade1.setGrade(6.5);
        grade1.setStudent(student);
        grade1.setSubject(English);
        GradeEntity grade2 = new GradeEntity();
        grade1.setGrade(8);
        grade1.setStudent(student);
        grade1.setSubject(Math);
        grades.add(grade1);
        grades.add(grade2);

        UniversityEntity university = new UniversityEntity();
        university.setName("BEST ONE");
        university.setCountry("Romania");
        student.setUniversity(university);

        EmbeddableAddressEntity address = new EmbeddableAddressEntity();
        address.setCountry("Romania");
        address.setCity("Bucuresti");
        address.setStreet("Recunostintei");

        student.setAddress(address);
        student.setSubjects(subjects);
        student.setGrades(grades);

        em.persist(student);
        em.persist(grades);
        em.persist(subjects);
        em.persist(university);
        utx.commit();
        em.clear();
    }
}
