package group.msg.test.jpa.exercise_test;


import group.msg.examples.jpa.exercise_entityPackage.Address;
import group.msg.examples.jpa.exercise_entityPackage.Student;
import group.msg.examples.jpa.exercise_entityPackage.Subject;
import group.msg.examples.jpa.exercise_entityPackage.University;
import group.msg.examples.jpa.validator.ValidationEntity;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RunWith(Arquillian.class)
public class Exercise_test extends JPABaseTest {
    private static final int NUMBER_OF_ENTITIES = 5;

    @PersistenceUnit(unitName = "test")
    private EntityManagerFactory emf;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true,"group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }
    @Test
    public void testUpdateSimpleEntity() {
        Query check = em.createNativeQuery("select stud.STUDENT_ID from student stud where stud.lastname = 'Maria'");
       List<Integer> simpleEntityId = check.getResultList();
        Assert.assertEquals("Entity not updated!", 1, simpleEntityId.size());
    }


    @Override
    protected void insertData()throws Exception{
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        University u1 = new University();
        University u2 = new University();
        Subject s1 = new Subject();
        Subject s2 = new Subject();
        Subject s3 = new Subject();
        Student stud = new Student();
        s1.setName("sub1");
        s1.setSubject_id(1);
        s2.setName("sub2");
        s2.setSubject_id(2);
        s3.setName("sub3");
        s3.setSubject_id(3);
        List<Subject> sub = new ArrayList<>();
        List<Subject> sub2 = new ArrayList<>();
        sub.add(s1);
        sub.add(s2);
        sub2.add(s3);
        sub2.add(s1);
        u1.setName("UVT");
        u1.setUniversity_id(12);
        u1.setCountry("country");
        u2.setName("UPT");
        u2.setUniversity_id(13);
        u2.setCountry("OtherCountry");

        stud.setLastName("Maria");
        stud.setFirstName("SSS");
        stud.setAddress(new Address("somewhere1","city1", "country1"));
        stud.setUniversity(u1);
        stud.setSubjects(sub2);
        stud.setEmail("maria@e-uvt.ro");

        em.persist(u1);
        em.persist(u2);
        em.persist(s1);
        em.persist(s2);
        em.persist(s3);
        em.persist(stud);

        for (int i = 1; i <= NUMBER_OF_ENTITIES; i++) {
            Student s = new Student();

            if (i % 2 == 0) {
                s.setLastName("Alina" + i);
                s.setFirstName("Ionescu");
                s.setAddress(new Address("somewhere","city", "country"));
                s.setUniversity(u1);
                s.setSubjects(sub);
                s.setEmail("alina" + i + "@e-uvt.ro");
            } else {
                s.setLastName("Mihai"+ i);
                s.setFirstName("Xulescu");
                s.setAddress(new Address("somewhere2","city2", "country2"));
                s.setUniversity(u2);
                s.setSubjects(sub2);
                s.setEmail("mihai" + i+ "@e-uvt.ro");
            }
            em.persist(s);

        }
        utx.commit();

        em.clear();
    }
   @Override
    protected void internalClearData() {
        em.createQuery("delete from Student").executeUpdate();
    }
}
