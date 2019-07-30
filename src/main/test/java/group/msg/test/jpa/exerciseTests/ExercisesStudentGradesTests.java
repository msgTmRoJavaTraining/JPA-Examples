package group.msg.test.jpa.exerciseTests;

import group.msg.examples.jpa.exerciseEntityPackage.*;
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
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class ExercisesStudentGradesTests extends JPABaseTest {

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
        em.remove(stud);
    }

    @Override
    protected void insertData()throws Exception{
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        University university = new University();
        Subject subject1 = new Subject();
        Subject subject2 = new Subject();
        Student stud = new Student();
        Grades grade1 = new Grades();
        Grades grade2 = new Grades();
        Grades grade3 = new Grades();
        List<Grades>gradeList= new ArrayList<>();
        List<Subject> subjects = new ArrayList<>();

        grade1.setIdGrade(1);
        grade1.setSubject(subject1);
        grade1.setStudent(stud);
        grade1.setGrade(10);

        grade2.setIdGrade(2);
        grade2.setSubject(subject1);
        grade2.setStudent(stud);
        grade2.setGrade(5);

        grade3.setIdGrade(3);
        grade3.setSubject(subject2);
        grade3.setStudent(stud);
        grade3.setGrade(7);

        subject1.setName("sub1");
        subject1.setSubject_id(1);

        gradeList.add(grade1);
        gradeList.add(grade2);
        gradeList.add(grade3);

        subject2.setName("sub2");
        subject2.setSubject_id(2);

        subjects.add(subject1);
        subjects.add(subject2);

        university.setName("UVT");
        university.setUniversity_id(12);
        university.setCountry("country");

        stud.setLastName("Maria");
        stud.setFirstName("SSS");
        stud.setAddress(new Address("somewhere1","city1", "country"));
        stud.setUniversity(university);
        stud.setSubjects(subjects);
        stud.setGrades(gradeList);
        stud.setEmail("maria@e-uvt.ro");

        em.persist(university);
        em.persist(subject1);
        em.persist(subject2);
        em.persist(stud);

        utx.commit();
        em.clear();
    }

}
