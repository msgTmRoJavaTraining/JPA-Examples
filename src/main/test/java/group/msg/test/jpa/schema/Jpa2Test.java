package group.msg.test.jpa.schema;

import group.msg.exerciseJPA2.Bike;
import group.msg.exerciseJPA2.Car;
import group.msg.exerciseJPA2.Truck;
import group.msg.exerciseJPA2.Vehicle;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class Jpa2Test extends JPABaseTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true,"group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }
    @Override
    protected void internalClearData() {
        em.createQuery("delete from Truck ").executeUpdate();
        em.createQuery("delete from Car ").executeUpdate();
        em.createQuery("delete from Bike ").executeUpdate();
        em.createQuery("delete from Vehicle ").executeUpdate();
    }
    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        Vehicle veh=new Vehicle();
            veh.setManufacturer("manuf1");
        Bike bik=new Bike();
            bik.setManufacturer("manuf2");
            bik.setNoOfPassengers(2);
            bik.setSaddleHeight(15);
        Car car=new Car();
            car.setManufacturer("manuf2");
            car.setNoOfDors(2);
            car.setNoOfPassenger(5);
        Truck trk=new Truck();
            trk.setManufacturer("manuf3");
            trk.setLoadCapacity(100);
            trk.setNoOfContainers(3);

        em.persist(veh);
        em.persist(bik);
        em.persist(car);
        em.persist(trk);
        utx.commit();
        em.clear();
    }
    @Test
    public void testRun(){


    }
}
