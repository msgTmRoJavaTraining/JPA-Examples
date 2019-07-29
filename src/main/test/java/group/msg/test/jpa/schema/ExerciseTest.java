package group.msg.test.jpa.schema;

import group.msg.examples.jpa.entity.SimpleEntity;
import group.msg.exercise.*;
import group.msg.logger.LoggerProducer;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class ExerciseTest extends JPABaseTest {
    private static final int NUMBER_OF_ENTITIES = 1;
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true,"group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }
    @Override
    protected void internalClearData() {
        em.createQuery("delete from Student ").executeUpdate();
        em.createQuery("delete from Subject ").executeUpdate();
        em.createQuery("delete from University ").executeUpdate();
        em.createQuery("delete from Grade ").executeUpdate();
    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        Address adrs=new Address();
            adrs.setCity("Bucuresti");
            adrs.setCountry("Romania");
            adrs.setStreet("Street1");
        Address adrs2=new Address();
            adrs.setCity("Timisoara");
            adrs.setCountry("Romania");
            adrs.setStreet("Street2");

        Subject subj1=new Subject();
        List<Subject> subjects=new ArrayList<>();
        subj1.setName("mate");
        subjects.add(subj1);


        University uni1 = new University();
            uni1.setName("Uni1");
            uni1.setCountry("Romania");

        Student stud1=new Student();
            stud1.setFirstName("FName1");
            stud1.setLastName("LName1");
            stud1.setHomeAddress(adrs);
            stud1.setUniversity(uni1);
            stud1.setSection("info");
            stud1.setSubjects(subjects);

        Student stud2=new Student();
            stud2.setFirstName("FName2");
            stud2.setLastName("LName2");
            stud2.setHomeAddress(adrs);
            stud2.setUniversity(uni1);
            stud1.setSection("info");
            stud1.setSubjects(subjects);

        Student stud3=new Student();
            stud3.setFirstName("FName3");
            stud3.setLastName("LName3");
            stud3.setHomeAddress(adrs2);
            stud3.setUniversity(uni1);
            stud1.setSection("info");
            stud1.setSubjects(subjects);

        List<Student> students=new ArrayList<>();
            students.add(stud1);
            students.add(stud2);
            students.add(stud3);

        Grade grades=new Grade();
            grades.setGrd(8.3);
            grades.setSubj(subj1);
            grades.setStudent(stud2);

        for (Student s:students) {
            em.persist(s);
        }
        for (Subject s:subjects) {
            em.persist(s);
        }

        em.persist(grades);
        em.persist(uni1);
        utx.commit();
        em.clear();
    }

    @Test
    public void notNullTest()
    {
        Student st=new Student();
        st.setLastName("ddsdss");
        st.setFirstName("fdsfsddsd");
        Address adrs=new Address();
        adrs.setStreet("theStreet");
        adrs.setCountry("theCountry");
        adrs.setCity("theCity");
        st.setHomeAddress(adrs);
        em.persist(st);
    }
    @Test
    public void bannedStudents(){
        Student toAdd=new Student();
        Address adrs=new Address();
        adrs.setStreet("theStreet");
        adrs.setCountry("theCountry");
        adrs.setCity("theCity");
        toAdd.setHomeAddress(adrs);
        toAdd.setFirstName("Cris");
        toAdd.setLastName("Pop");


            em.persist(toAdd);
    }
    @Test
    public void testCascade(){
        em.remove(em.find(Student.class,2));

    }

    @Test
    public void testSelectCity(){
        TypedQuery<Student> like = em.createQuery("select se from Student se where se.homeAddress.city like 'Timisoara'", Student.class);
        List<Student> result = like.getResultList();
        System.out.println(result);
    }
    @Test
    public void testSelectSubject(){
       // TypedQuery<Student> like = em.createQuery("select st from Student st ,Subject su where su.name  like  'mate' ", Student.class);
        TypedQuery<Student> like = em.createQuery("select st from Student st,Subject su join fetch st.subjects where  su.name  like  'mate'",Student.class);
        List<Student> result = like.getResultList();
        result.forEach(e-> System.out.println(e));
    }
    @Test
    public void testSelectAvg(){
        // TypedQuery<Student> like = em.createQuery("select st from Student st ,Subject su where su.name  like  'mate' ", Student.class);
        TypedQuery<Double> like = em.createQuery("select avg(gd.grd) from Student st,Grade gd  join fetch st.grades  ",Double.class);
        List<Double> result = like.getResultList();
        result.forEach(e-> System.out.println(e));
    }
    @Test
    public void testSelectAvgAbove(){
        // TypedQuery<Student> like = em.createQuery("select st from Student st ,Subject su where su.name  like  'mate' ", Student.class);
        Query like = em.createQuery("select  avg(gr.grd)  from Student st  join fetch st.grades gr group by st.firstName having avg(gr.grd)>8 ");
        List result = like.getResultList();
        result.forEach(e-> System.out.println(e));
    }
    @Test
    public void testCountCity() {
        // TypedQuery<Student> like = em.createQuery("select st from Student st ,Subject su where su.name  like  'mate' ", Student.class);
        Query like = em.createQuery("select count(st.homeAddress.city) from Student st group by  st.homeAddress.city  ");
        List result = like.getResultList();
        result.forEach(e -> System.out.println(e));
    }



}




