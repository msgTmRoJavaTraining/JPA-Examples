package group.msg.test.jpa.exerciseTests;


import group.msg.examples.jpa.exerciseEntityPackage.Address;
import group.msg.examples.jpa.exerciseEntityPackage.Student;
import group.msg.examples.jpa.exerciseEntityPackage.Subject;
import group.msg.examples.jpa.exerciseEntityPackage.University;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@RunWith(Arquillian.class)
public class ExerciseTests extends JPABaseTest {
    private static final int NUMBER_OF_ENTITIES = 5;


    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true,"group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }
    @Test
    public void testGetStudent() {
        Query check = em.createNativeQuery("select stud.STUDENT_ID from student stud where stud.lastname = 'Maria'");
        List<Integer> students = check.getResultList();
        Assert.assertEquals("Entity not updated!", 1, students.size());
    }


    @Override
    protected void insertData()throws Exception{
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        University university1 = new University();
        University university2 = new University();
        Subject subject1 = new Subject();
        Subject subject2 = new Subject();
        Subject subject3 = new Subject();
        Student stud = new Student();
        List<Subject> subjectList1 = new ArrayList<>();
        List<Subject> subjectList2 = new ArrayList<>();

        subject1.setName("sub1");
        subject1.setSubject_id(1);

        subject2.setName("subjectList2");
        subject2.setSubject_id(2);

        subject3.setName("sub3");
        subject3.setSubject_id(3);

        subjectList1.add(subject1);
        subjectList1.add(subject2);

        subjectList2.add(subject3);
        subjectList2.add(subject1);

        university1.setName("UVT");
        university1.setUniversity_id(12);
        university1.setCountry("country");

        university2.setName("UPT");
        university2.setUniversity_id(13);
        university2.setCountry("OtherCountry");

        stud.setLastName("Maria");
        stud.setFirstName("SSS");
        stud.setAddress(new Address("somewhere1","city1", "country"));
        stud.setUniversity(university1);
        stud.setSubjects(subjectList2);
        stud.setEmail("maria@e-uvt.ro");

        em.persist(university1);
        em.persist(university2);
        em.persist(subject1);
        em.persist(subject2);
        em.persist(subject3);
        em.persist(stud);

        for (int i = 1; i <= NUMBER_OF_ENTITIES; i++) {
            Student s = new Student();

            if (i % 2 == 0) {
                s.setLastName("Alina" + i);
                s.setFirstName("Ionescu");
                s.setAddress(new Address("somewhere","city", "country"));
                s.setUniversity(university1);
                s.setSubjects(subjectList1);
                s.setEmail("alina" + i + "@e-uvt.ro");
            } else {
                s.setLastName("Mihai"+ i);
                s.setFirstName("Xulescu");
                s.setAddress(new Address("somewhere2","city2", "OtherCountry"));
                s.setUniversity(university2);
                s.setSubjects(subjectList2);
                s.setEmail("mihai" + i+ "@e-uvt.ro");
            }
            em.persist(s);

        }
        utx.commit();

        em.clear();
    }
   @Override
    protected void internalClearData() {
        em.createNativeQuery("delete from STUDENT").executeUpdate();
       em.createNativeQuery("delete from SUBJECT_ENTITY").executeUpdate();
       em.createNativeQuery("delete from UNIVERSITY_ENTITY").executeUpdate();
       em.createNativeQuery("delete from STUDENT_SUBJECT").executeUpdate();
    }
}
