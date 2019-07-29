package group.msg.test.jpa.ExerciseTest;

import group.msg.examples.jpa.entity.*;
import group.msg.exercises.entities.*;
import group.msg.exercises.secondDay.Vehicle;
import group.msg.test.jpa.JPABaseTest;
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
public class TesttStudent extends JPABaseTest {

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
    public void TestGrade() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {

        List<Grades> myGrades = new ArrayList<>();
        List<Student> myList = new ArrayList<>();

        Student s1 = new Student();
        Student s2 = new Student();

        Adress a1 = new Adress();
        Adress a2 = new Adress();

        Subject sub1 = new Subject();
        Subject sub2 = new Subject();


        Grades g1 = new Grades();
        Grades g2 = new Grades();

        University university = new University();

        s1.setLast_name("Ion");
        s1.setLast_name("Gheorghe");
        s1.setAdress(a1);



        a1.setCity("Timisoara");
        a1.setStreet("Principal");
        a1.setCountry("Romania");


        sub1.setName("Math");


        g1.setValue(10);
        g1.setStudent(s1);
        g1.setSubject(sub1);

        myGrades.add(g1);
        myGrades.add(g2);

        myList.add(s1);
        myList.add(s2);

        s1.setGrades(myGrades);
        university.setName("Poli");
        university.setCountry("Romania");
        university.setStudent_list(myList);

        em.persist(s1);
        em.persist(sub1);
        em.persist(g1);
        em.persist(university);
        utx.commit();
        em.clear();



    }
}
