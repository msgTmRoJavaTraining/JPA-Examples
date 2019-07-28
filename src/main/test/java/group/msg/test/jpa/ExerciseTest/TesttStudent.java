package group.msg.test.jpa;

import group.msg.examples.jpa.entity.*;
import group.msg.exercises.entities.*;
import group.msg.exercises.secondDay.Vehicle;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class TesttStudent extends group.msg.test.jpa.JPABaseTest {

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


    }


    @Test
    public void TestGrade() throws  SystemException, NotSupportedException {

        utx.begin();
        em.joinTransaction();

        Student s1 = new Student();
        Student s2 = new Student();






        Adress a1 = new Adress();
        Adress a2 = new Adress();




        Subject sub1 = new Subject();
        Subject sub2 = new Subject();

        Grades g1 = new Grades();
        Grades g2 = new Grades();

        University university = new University();

    }
}
