package group.msg.test.jpa.entity;


import group.msg.examples.jpa.entity.Bike;
import group.msg.examples.jpa.entity.Car;
import group.msg.examples.jpa.entity.Truck;
import group.msg.examples.jpa.entity.Vehicle;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class VehicleTest extends JPABaseTest
{

    @Override
    protected void insertData() throws Exception {

        utx.begin();
        em.joinTransaction();

        Vehicle vehicle=new Vehicle();
        vehicle.setManufactured("Audi");
        vehicle.setVehicleType("Combi");

        em.persist(vehicle);


        Truck truck=new Truck();
        truck.setLoadCapacity(1000);
        truck.setNumberOfContainers(1);

        em.persist(truck);

        Car car=new Car();
        car.setNumberOfDoors(4);
        car.setNumberOfPassengers(5);

        em.persist(car);

        Bike bike =new Bike();
        bike.setNumberOfPassengers(1);
        bike.setSaddleHeight(100);
        em.persist(bike);

        utx.commit();

        em.clear();
    }


    @Test
    public void testVehicle() {

    }


    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

}
