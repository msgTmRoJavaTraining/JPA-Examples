package group.msg.test.jpa.exerciseTests;


import group.msg.examples.jpa.exerciseEntityPackage.*;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class ExerciseJPQLTests extends JPABaseTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testStudentsFromCity() {
        TypedQuery<Student> result = em.createQuery("select stud from Student stud where stud.address.city in ('city')", Student.class);
        List<Student> students = result.getResultList();
        for (Student s : students) {
            System.out.println(s);
        }
    }

    @Test
    public void testJoinOnSubject() {
        TypedQuery<Student> result = em.createQuery("select  distinct stud from  Subject sub, Student stud join fetch stud.subjects where sub.name like 'sub1'", Student.class);
        List<Student> students = result.getResultList();
        for (Student s : students) {
            System.out.println(s);
        }
    }

    @Test
    public void testGetAvgStudents() {
        Query resultSet = em.createQuery("select avg(gr.grade) from Grades gr join gr.student stud group by stud.student_id ");
        List<Double> avg = resultSet.getResultList();
        for (Double d : avg) {
            System.out.println(d.toString());
        }
    }
    @Test
    public void testAvgGradeGreater() {
        Query resultSet = em.createQuery("select stud.lastName, avg(gr.grade) from Student stud join fetch stud.grades gr group by stud.lastName having avg(gr.grade)>8 ");
        List<Object[]> pair = resultSet.getResultList();
        for (Object[] elem : pair) {
            System.out.println("Student with pair bigger than 8 : " + elem[0]);
        }
    }
    @Test
    public void testCityCount() {
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
        University university = new University();
        Subject subject1 = new Subject();
        Subject subject2 = new Subject();
        Student student1 = new Student();
        Student student2 = new Student();
        Student student3 = new Student();
        Grades grade1 = new Grades();
        Grades grade2 = new Grades();
        Grades grade3 = new Grades();

        Grades grade4 = new Grades();
        Grades grade5 = new Grades();
        Grades grade6 = new Grades();

        Grades grade7 = new Grades();
        Grades grade8 = new Grades();
        Grades grade9 = new Grades();

        List<Grades> gradeList = new ArrayList<>();
        List<Grades> gradeList2 = new ArrayList<>();
        List<Grades> gradeList3 = new ArrayList<>();

        grade1.setIdGrade(1);
        grade1.setSubject(subject1);
        grade1.setStudent(student1);
        grade1.setGrade(10);

        grade2.setIdGrade(2);
        grade2.setSubject(subject1);
        grade2.setStudent(student1);
        grade2.setGrade(5);

        grade3.setIdGrade(3);
        grade3.setSubject(subject2);
        grade3.setStudent(student1);
        grade3.setGrade(7);

        grade4.setIdGrade(4);
        grade4.setSubject(subject1);
        grade4.setStudent(student2);
        grade4.setGrade(10);

        grade5.setIdGrade(5);
        grade5.setSubject(subject2);
        grade5.setStudent(student2);
        grade5.setGrade(9);

        grade6.setIdGrade(6);
        grade6.setSubject(subject1);
        grade6.setStudent(student2);
        grade6.setGrade(8);

        grade7.setIdGrade(7);
        grade7.setSubject(subject1);
        grade7.setStudent(student3);
        grade7.setGrade(8);

        grade8.setIdGrade(8);
        grade8.setSubject(subject1);
        grade8.setStudent(student3);
        grade8.setGrade(7);

        grade9.setIdGrade(9);
        grade9.setSubject(subject2);
        grade9.setStudent(student3);
        grade9.setGrade(2);

        subject1.setName("sub1");
        subject1.setSubject_id(1);

        gradeList.add(grade1);
        gradeList.add(grade2);
        gradeList.add(grade3);

        gradeList2.add(grade4);
        gradeList2.add(grade5);
        gradeList2.add(grade6);

        gradeList3.add(grade7);
        gradeList3.add(grade8);
        gradeList3.add(grade9);

        subject2.setName("sub2");
        subject2.setSubject_id(2);

        List<Subject> sub = new ArrayList<>();
        sub.add(subject1);
        sub.add(subject2);

        university.setName("UVT");
        university.setUniversity_id(12);
        university.setCountry("country");


        student1.setLastName("Maria");
        student1.setFirstName("SSS");
        student1.setAddress(new Address("somewhere1", "city1", "country"));
        student1.setUniversity(university);
        student1.setSubjects(sub);
        student1.setGrades(gradeList);
        student1.setEmail("maria@e-uvt.ro");

        student2.setLastName("Ana");
        student2.setFirstName("SSS");
        student2.setAddress(new Address("somewhere2", "city1", "country"));
        student2.setUniversity(university);
        student2.setSubjects(sub);
        student2.setGrades(gradeList2);
        student2.setEmail("ana@e-uvt.ro");

        student3.setLastName("Mihai");
        student3.setFirstName("SSS");
        student3.setAddress(new Address("somewhere2", "city", "country"));
        student3.setUniversity(university);
        student3.setSubjects(sub);
        student3.setGrades(gradeList3);
        student3.setEmail("mihai@e-uvt.ro");

        em.persist(university);
        em.persist(subject1);
        em.persist(subject2);
        em.persist(grade1);
        em.persist(grade2);
        em.persist(grade3);
        em.persist(grade4);
        em.persist(grade5);
        em.persist(grade6);
        em.persist(grade7);
        em.persist(grade8);
        em.persist(grade9);
        em.persist(student1);
        em.persist(student2);
        em.persist(student3);

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
