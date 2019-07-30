package group.msg.test.jpa.exerciseInheritanceTests;


import group.msg.examples.jpa.exerciseInheritancePackage.Bike;
import group.msg.examples.jpa.exerciseInheritancePackage.Car;
import group.msg.examples.jpa.exerciseInheritancePackage.Truck;
import group.msg.examples.jpa.exerciseInheritancePackage.Vehicle;
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


    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testVehicleEntity() {
        Query check = em.createNativeQuery("select b.IdVehicle from BIKE B ");
        Assert.assertEquals("Entity not updated!", 2, check.getResultList().size());
    }
    @Override
    protected void insertData()throws Exception{
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        Vehicle veh1 = new Vehicle();
        Vehicle veh2 = new Vehicle();
        Truck truck1 = new Truck();
        Truck truck2 = new Truck();
        Car car1 = new Car();
        Car car2 = new Car();
        Bike bike1 = new Bike();
        Bike bike2 = new Bike();

        veh1.setIdVehicle(1);
        veh1.setManufacturer("veh1");
        veh1.setVehicle_type("vehicle");

        veh2.setIdVehicle(2);
        veh2.setManufacturer("veh2");
        veh2.setVehicle_type("vehicle");

        truck1.setIdVehicle(3);
        truck1.setManufacturer("trk1");
        truck1.setVehicle_type("truck");
        truck1.setLoadCapacity(12);
        truck1.setNoOfContainers(3);

        truck2.setIdVehicle(4);
        truck2.setManufacturer("trk2");
        truck2.setVehicle_type("truck");
        truck2.setLoadCapacity(10);
        truck2.setNoOfContainers(2);

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

        bike1.setIdVehicle(7);
        bike1.setManufacturer("bik1");
        bike1.setVehicle_type("bike");
        bike1.setNoOfPassengers(2);
        bike1.setSaddleHeight(34);

        bike2.setIdVehicle(8);
        bike2.setManufacturer("bik2");
        bike2.setVehicle_type("bike");
        bike2.setNoOfPassengers(1);
        bike2.setSaddleHeight(24);


        em.persist(veh1);
        em.persist(veh2);
        em.persist(truck1);
        em.persist(truck2);
        em.persist(car1);
        em.persist(car2);
        em.persist(bike1);
        em.persist(bike2);
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
