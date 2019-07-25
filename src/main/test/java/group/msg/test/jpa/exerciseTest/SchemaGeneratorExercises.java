package group.msg.test.jpa.exerciseTest;

import group.msg.examples.jpa.entity.SimpleEntity;
import group.msg.examples.jpa.entity.SimpleType;
import group.msg.exercises.entities.Adress;
import group.msg.exercises.entities.Student;
import group.msg.exercises.entities.Subject;
import group.msg.exercises.entities.University;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class SchemaGeneratorExercises extends JPABaseTest {

    private static final int NUMBER_OF_ENTITIES = 5;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true,"group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testCreateSimpleEntity() {
        System.out.println("Checking number of created entities...");

        Query q = em.createNativeQuery("select * from university");

    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");

        Student s1 = new Student();
        Student s2 = new Student();
        List<Student> myList= new ArrayList<>();
        myList.add(s1);
        myList.add(s2);

        Adress a1 = new Adress(1,"str1","timisoara","rom");
        Adress a2 = new Adress(2,"str2","resita","rom");

        Subject sub1 = new Subject();
        Subject sub2 = new Subject();

        University university = new University();


        s1.setFirst_name("Ion");
        s1.setLast_name("Gheorghe");
        s1.setSection("math");
        s1.setAdress(a1);
        s1.setUniversity_id(university);



        s2.setFirst_name("Gheorghe");
        s2.setLast_name("Ion");
        s2.setSection("other");
        s2.setAdress(a2);
        s2.setUniversity_id(university);



        sub1.setName("sub1");
        sub1.setManyToManyStudent(myList);


        sub2.setName("sub2");
        sub2.setManyToManyStudent(myList);


        university.setCountry("romania");
        university.setName("po;o");


        em.persist(university);
        utx.commit();
        em.clear();



    }

    @Override
    protected void internalClearData() {
        em.createQuery("delete from SimpleEntity").executeUpdate();
    }

}
