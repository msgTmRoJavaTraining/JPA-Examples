package group.msg.test.jpa.exerciseTest;


import group.msg.exercises.entities.Adress;
import group.msg.exercises.entities.Student;
import group.msg.exercises.entities.Subject;
import group.msg.exercises.entities.University;
import group.msg.exercises.entities.jpa_second_day.Bike;
import group.msg.exercises.entities.jpa_second_day.Car;
import group.msg.exercises.entities.jpa_second_day.Truck;
import group.msg.exercises.entities.jpa_second_day.Vehicle;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class TestVehicleExercise extends JPABaseTest {

    private static final int NUMBER_OF_ENTITIES = 5;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testCreateSimpleEntity() {
        System.out.println("Checking number of created entities...");

        Query q = em.createNativeQuery("select * from UNIVERSITY");

    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");

        Vehicle vehicle = new Vehicle();
        Car car = new Car();
        Truck truck = new Truck();
        Bike bike = new Bike();

        vehicle.setVehicle_type("Transport persoane");
        vehicle.setManufacturer("Ford");

        car.setNoOfDoors(4);
        car.setNoOfPassenger(5);

        bike.setNoOfPassengers(1);
        bike.setSaddleHeight(100);

        truck.setLoadCapacity(1000);
        truck.setNumberOfContainers(10);

        em.persist(vehicle);
        em.persist(car);
        em.persist(truck);
        em.persist(bike);

        utx.commit();
        em.clear();


    }
}
