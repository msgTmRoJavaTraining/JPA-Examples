package group.msg.test.jpa.Day15;


import group.msg.examples.jpa.entity.day15.Bike;
import group.msg.examples.jpa.entity.day15.Car;
import group.msg.examples.jpa.entity.day15.Truck;
import group.msg.examples.jpa.entity.day15.Vehicle;
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

public class VehicleTest extends JPABaseTest {


    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void createVehicleHierarchy() {
        Query check = em.createNativeQuery("select * from vehicle ");
        Assert.assertEquals("Entity not updated!", 1, check.getResultList().size());
    }


    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");

        Truck truck = new Truck();
        truck.setLoadCapacity(20);
        truck.setNoOfContainers(5);
        truck.setManufacturer("Mercedes");
        truck.setVehicle_type("truck");

        em.persist(truck);

        Car car = new Car();
        car.setNoOfDoors(4);
        car.setNoOfPassegers(5);
        car.setManufacturer("Fiat");
        car.setVehicle_type("Car");

        em.persist(car);

        Bike bike = new Bike();
        bike.setNoOfPassegers(1);
        bike.setSaddleHeight(20);
        bike.setManufacturer("ala");
        bike.setVehicle_type("bike");

        em.persist(bike);

        Vehicle vehicle=new Vehicle();


        em.persist(vehicle);
        utx.commit();

        em.clear();
    }

    @Override
    protected void internalClearData() {
        em.createQuery("delete from Vehicle").executeUpdate();
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
