package group.msg.test.jpa.exercise_test;

import group.msg.examples.jpa.exercise_entityPackage.*;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class Exercises_studentGradesTest extends JPABaseTest {
    private static final int NUMBER_OF_ENTITIES = 5;

    @PersistenceContext
    private EntityManager em;
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }
    @Test
    public void testGradesCreation() {
        Query check = em.createNativeQuery("select gr.grade from GRADES_ENTITY gr where gr.SUBJECTID = 1");
        List<Integer> simpleEntityId = check.getResultList();
        Assert.assertEquals("Entity not updated!", 2, simpleEntityId.size());
    }
    @Test
    @Transactional
    public void testStudentRemoval() {
        Student stud = em.find(Student.class,1);
        em.getTransaction();
        em.remove(stud);
    }

    @Override
    protected void insertData()throws Exception{
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        University u1 = new University();
        Subject s1 = new Subject();
        Subject s2 = new Subject();
        Student stud = new Student();
        Grades gr1 = new Grades();
        Grades gr2 = new Grades();
        Grades gr3 = new Grades();
        List<Grades>gradeList= new ArrayList<>();

        gr1.setIdGrade(1);
        gr1.setSubject(s1);
        gr1.setStudent(stud);
        gr1.setGrade(10);

        gr2.setIdGrade(2);
        gr2.setSubject(s1);
        gr2.setStudent(stud);
        gr2.setGrade(5);

        gr3.setIdGrade(3);
        gr3.setSubject(s2);
        gr3.setStudent(stud);
        gr3.setGrade(7);

        s1.setName("sub1");
        s1.setSubject_id(1);

        gradeList.add(gr1);
        gradeList.add(gr2);
        gradeList.add(gr3);

        s2.setName("sub2");
        s2.setSubject_id(2);

        List<Subject> sub = new ArrayList<>();
        sub.add(s1);
        sub.add(s2);

        u1.setName("UVT");
        u1.setUniversity_id(12);
        u1.setCountry("country");


        stud.setLastName("Maria");
        stud.setFirstName("SSS");
        stud.setAddress(new Address("somewhere1","city1", "country"));
        stud.setUniversity(u1);
        stud.setSubjects(sub);
        stud.setGrades(gradeList);
        stud.setEmail("maria@e-uvt.ro");

        em.persist(u1);
        em.persist(s1);
        em.persist(s2);
        em.persist(gr1);
        em.persist(gr2);
        em.persist(gr3);
        em.persist(stud);

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

    @Override
    protected void startTransaction() {
        // Multiple transactions needed
    }

    @Override
    public void commitTransaction() {
        // Don't commit non existent transaction
    }
}
