package group.msg.test.jpa.day15;

import group.msg.examples.jpa.entity.day15.Bike;
import group.msg.examples.jpa.entity.day15.Car;
import group.msg.examples.jpa.entity.day15.Truck;
import group.msg.examples.jpa.entity.day15.Vehicle;
import group.msg.examples.jpa.entity.mapping.ConcreteEntity;
import group.msg.examples.jpa.entity.mapping.SubVagueEntity;
import group.msg.examples.jpa.entity.mapping.SuperVagueEntity;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

@RunWith(Arquillian.class)
public class Tests extends JPABaseTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testCreateMappedEntity() {
        Query check = em.createNativeQuery("select * from Vehicle");
        Assert.assertEquals("Car Select", 1, check.getResultList().size());
    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();

        Truck truck = new Truck();
        truck.setLoadCapacity(100);
        truck.setNoOfContainers(2);
        truck.setVehicle_type("Truck");
        truck.setManufacturer("Man");
        em.persist(truck);

        Car car = new Car();
        car.setNoOfDoors(5);
        car.setNoOfPassengers(5);
        car.setVehicle_type("Car");
        car.setManufacturer("Volkswagen");
        em.persist(car);

        Bike bike = new Bike();
        bike.setNoOfPassengers(1);
        bike.setSaddleHeight(4);
        bike.setVehicle_type("Bike");
        bike.setManufacturer("Pegas");
        em.persist(bike);

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicle_type("Vehicle");
        vehicle.setManufacturer("Random");
        em.persist(vehicle);

        utx.commit();
    }

    @Override
    protected void internalClearData() {
        em.createQuery("delete from Vehicle ").executeUpdate();
        em.createQuery("delete from Car ").executeUpdate();
        em.createQuery("delete from Truck ").executeUpdate();
        em.createQuery("delete from Bike ").executeUpdate();
    }
}
