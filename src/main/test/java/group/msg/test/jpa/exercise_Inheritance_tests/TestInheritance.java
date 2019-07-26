package group.msg.test.jpa.exercise_Inheritance_tests;


import group.msg.examples.jpa.exercise_InheritancePackage.Bike;
import group.msg.examples.jpa.exercise_InheritancePackage.Car;
import group.msg.examples.jpa.exercise_InheritancePackage.Truck;
import group.msg.examples.jpa.exercise_InheritancePackage.Vehicle;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;

@RunWith(Arquillian.class)
public class TestInheritance extends JPABaseTest {

    private static final int NUMBER_OF_ENTITIES = 5;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testCreateMappedEntity() {
        Query check = em.createNativeQuery("select b.IdVehicle from BIKE B ");
        Assert.assertEquals("Entity not updated!", 2, check.getResultList().size());
    }
    @Override
    protected void insertData()throws Exception{
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        Vehicle v1 = new Vehicle();
        Vehicle v2 = new Vehicle();
        Truck tr1 = new Truck();
        Truck tr2 = new Truck();
        Car car1 = new Car();
        Car car2 = new Car();
        Bike b1 = new Bike();
        Bike b2 = new Bike();

        v1.setIdVehicle(1);
        v1.setManufacturer("veh1");
        v1.setVehicle_type("vehicle");

        v2.setIdVehicle(2);
        v2.setManufacturer("veh2");
        v2.setVehicle_type("vehicle");

        tr1.setIdVehicle(3);
        tr1.setManufacturer("trk1");
        tr1.setVehicle_type("truck");
        tr1.setLoadCapacity(12);
        tr1.setNoOfContainers(3);

        tr2.setIdVehicle(4);
        tr2.setManufacturer("trk2");
        tr2.setVehicle_type("truck");
        tr2.setLoadCapacity(10);
        tr2.setNoOfContainers(2);

        car1.setIdVehicle(5);
        car1.setManufacturer("car1");
        car1.setNoOfPassengers(4);
        car1.setVehicle_type("car");
        car1.setNoOfDoors(2);

        car2.setIdVehicle(6);
        car2.setManufacturer("car2");
        car2.setNoOfPassengers(4);
        car2.setVehicle_type("car");
        car2.setNoOfDoors(4);

        b1.setIdVehicle(7);
        b1.setManufacturer("bik1");
        b1.setVehicle_type("bike");
        b1.setNoOfPassengers(2);
        b1.setSaddleHeight(34);

        b2.setIdVehicle(8);
        b2.setManufacturer("bik2");
        b2.setVehicle_type("bike");
        b2.setNoOfPassengers(1);
        b2.setSaddleHeight(24);


        em.persist(v1);
        em.persist(v2);
        em.persist(tr1);
        em.persist(tr2);
        em.persist(car1);
        em.persist(car2);
        em.persist(b1);
        em.persist(b2);
        utx.commit();
        em.clear();
    }
    @Override
    protected void internalClearData() {
        em.createNativeQuery("delete from VEHICLE").executeUpdate();
        em.createNativeQuery("delete from TRUCK").executeUpdate();
        em.createNativeQuery("delete from CAR").executeUpdate();
        em.createNativeQuery("delete from BIKE").executeUpdate();
    }

}
