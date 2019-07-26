package group.msg.test.jpa.entity;


import group.msg.examples.jpa.entity.HomeAddress;
import group.msg.examples.jpa.entity.StudentEntity;
import group.msg.examples.jpa.entity.Subject;
import group.msg.examples.jpa.entity.University;
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
public class StudentTest extends JPABaseTest
{

    private static final int NUMBER_OF_ENTITIES = 1;
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true,"group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();

        ArrayList<StudentEntity> studentEntities=new ArrayList<>();

        University university=new University();
        university.setName("UPT");
        university.setCountry("Romania");

        ArrayList<Subject>subjects=new ArrayList<>();

        Subject sub1=new Subject();
        Subject s2=new Subject();
        s2.setName("fizica");
        sub1.setName("chimie");

        subjects.add(sub1);
        subjects.add(s2);

        for(Subject s:subjects)
            em.persist(s);

        StudentEntity s1=new StudentEntity();
        StudentEntity studentEntity2=new StudentEntity();

        studentEntity2.setSection("IS");
        studentEntity2.setSubjects(subjects);
        studentEntity2.setUniversity(university);
        studentEntity2.setHomeAddress(new HomeAddress("Vidra","Iasi","Romania"));
        studentEntity2.setFirst_name("Dragos");
        studentEntity2.setLast_name("Morosan");
        s1.setFirst_name("Ion");
        s1.setLast_name("Baciu");
        s1.setHomeAddress(new HomeAddress("Parvan","Timisoara","Romania"));
        s1.setUniversity(university);
        s1.setSection("CTI");
        s1.setSubjects(subjects);
        studentEntities.add(s1);
        studentEntities.add(studentEntity2);

        for(StudentEntity stu:studentEntities)
            em.persist(stu);

        university.setStudentEntityList(studentEntities);

        em.persist(university);

        utx.commit();

        em.clear();


    }

    @Test
    public void testExcept()
    {
        StudentEntity stud1=new StudentEntity();

        stud1.setLast_name("A");
        em.persist(stud1);
    }

    @Override
    protected void internalClearData() {
        em.createQuery("delete from StudentEntity").executeUpdate();
    }

    @Test
    public void testCreateSimpleEntity()
    {
        System.out.println("Checking number of created entities...");

        Query q = em.createNativeQuery("select * from SUBJECT",Subject.class);
        List<Subject> subTest =q.getResultList();
        Assert.assertEquals("Entities not found in the database!", 2, subTest.size());
    }
}