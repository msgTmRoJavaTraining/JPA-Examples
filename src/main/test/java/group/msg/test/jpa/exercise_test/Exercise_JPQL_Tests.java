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

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class Exercise_JPQL_Tests extends JPABaseTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testJpql() {
        TypedQuery<Student> jpql = em.createQuery("select stud from Student stud where stud.address.city in ('city')", Student.class);
        Assert.assertEquals("Entity not found in the database!", 2, jpql.getResultList().size());
        List<Student> stud = jpql.getResultList();
        for (Student s : stud) {
            System.out.println(s);
        }
    }

    @Test
    public void testJoinJpql() {
        TypedQuery<Student> join = em.createQuery("select  distinct stud from  Subject sub, Student stud join fetch stud.subjects where sub.name like 'sub1'", Student.class);
        Assert.assertEquals("Query did not return the expected results!", 6, join.getResultList().size());
        List<Student> joinList = join.getResultList();
        for (Student s : joinList) {
            System.out.println(s);
        }
    }

    @Test
    public void testAvgJpql() {

        Query joinAvg = em.createQuery("select avg(gr.grade) from Grades gr join gr.student stud group by stud.student_id ");
        List<Double> avg = joinAvg.getResultList();
        for (Double d : avg) {
            System.out.println(d.toString());
        }
    }

    @Test
    public void testAvgGraderGreaterJpql() {
        Query joinGreater = em.createQuery("select stud.lastName, avg(gr.grade) from Student stud join fetch stud.grades gr group by stud.lastName having avg(gr.grade)>8 ");
        List<Object[]> avg = joinGreater.getResultList();
        for (Object[] d : avg) {
            System.out.println("Student with avg bigger than 8 : " + d[0]);
        }
    }

    @Test
    public void testCityCountJpql() {
        Query records = em.createQuery("select count( distinct studd.lastName), studd.address.city from Student studd where studd.address.city in (select stud.address.city from Student stud) group by studd.address.city");
        List<Object[]> rec = records.getResultList();
        for (Object[] obj : rec) {
            System.out.println(obj[0] + " students in city " + obj[1]);
        }
    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        University u1 = new University();
        Subject s1 = new Subject();
        Subject s2 = new Subject();
        Student stud = new Student();
        Student stud2 = new Student();
        Student stud3 = new Student();
        Grades gr1 = new Grades();
        Grades gr2 = new Grades();
        Grades gr3 = new Grades();

        Grades gr4 = new Grades();
        Grades gr5 = new Grades();
        Grades gr6 = new Grades();

        Grades gr7 = new Grades();
        Grades gr8 = new Grades();
        Grades gr9 = new Grades();

        List<Grades> gradeList = new ArrayList<>();
        List<Grades> gradeListStud2 = new ArrayList<>();
        List<Grades> gradeListStud3 = new ArrayList<>();

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

        gr4.setIdGrade(4);
        gr4.setSubject(s1);
        gr4.setStudent(stud2);
        gr4.setGrade(10);

        gr5.setIdGrade(5);
        gr5.setSubject(s2);
        gr5.setStudent(stud2);
        gr5.setGrade(9);

        gr6.setIdGrade(6);
        gr6.setSubject(s1);
        gr6.setStudent(stud2);
        gr6.setGrade(8);

        gr7.setIdGrade(7);
        gr7.setSubject(s1);
        gr7.setStudent(stud3);
        gr7.setGrade(8);

        gr8.setIdGrade(8);
        gr8.setSubject(s1);
        gr8.setStudent(stud3);
        gr8.setGrade(7);

        gr9.setIdGrade(9);
        gr9.setSubject(s2);
        gr9.setStudent(stud3);
        gr9.setGrade(2);

        s1.setName("sub1");
        s1.setSubject_id(1);

        gradeList.add(gr1);
        gradeList.add(gr2);
        gradeList.add(gr3);

        gradeListStud2.add(gr4);
        gradeListStud2.add(gr5);
        gradeListStud2.add(gr6);

        gradeListStud3.add(gr7);
        gradeListStud3.add(gr8);
        gradeListStud3.add(gr9);

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
        stud.setAddress(new Address("somewhere1", "city1", "country"));
        stud.setUniversity(u1);
        stud.setSubjects(sub);
        stud.setGrades(gradeList);
        stud.setEmail("maria@e-uvt.ro");

        stud2.setLastName("Ana");
        stud2.setFirstName("SSS");
        stud2.setAddress(new Address("somewhere2", "city1", "country"));
        stud2.setUniversity(u1);
        stud2.setSubjects(sub);
        stud2.setGrades(gradeListStud2);
        stud2.setEmail("ana@e-uvt.ro");

        stud3.setLastName("Mihai");
        stud3.setFirstName("SSS");
        stud3.setAddress(new Address("somewhere2", "city", "country"));
        stud3.setUniversity(u1);
        stud3.setSubjects(sub);
        stud3.setGrades(gradeListStud3);
        stud3.setEmail("mihai@e-uvt.ro");

        em.persist(u1);
        em.persist(s1);
        em.persist(s2);
        em.persist(gr1);
        em.persist(gr2);
        em.persist(gr3);
        em.persist(gr4);
        em.persist(gr5);
        em.persist(gr6);
        em.persist(gr7);
        em.persist(gr8);
        em.persist(gr9);
        em.persist(stud);
        em.persist(stud2);
        em.persist(stud3);

        utx.commit();

        em.clear();
    }

    @Override
    protected void startTransaction() {
    }

    @Override
    public void commitTransaction() {
    }

    @Override
    protected void internalClearData() {
        em.createNativeQuery("delete from STUDENT").executeUpdate();
        em.createNativeQuery("delete from SUBJECT_ENTITY").executeUpdate();
        em.createNativeQuery("delete from UNIVERSITY_ENTITY").executeUpdate();
        em.createNativeQuery("delete from STUDENT_SUBJECT").executeUpdate();
    }
}
