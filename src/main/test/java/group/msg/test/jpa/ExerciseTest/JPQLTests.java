package group.msg.test.jpa.ExerciseTest;



import group.msg.examples.jpa.entity.SimpleEntity;
import group.msg.examples.jpa.entity.mapping.*;
import group.msg.exercises.entities.Adress;
import group.msg.exercises.entities.Grades;
import group.msg.exercises.entities.Student;
import group.msg.exercises.entities.Subject;
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
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Arquillian.class)
public class JPQLTests extends JPABaseTest {

    private static List<Object> persistedObjects = new ArrayList<>();


    @Test
    public void testCertainAdress() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException {
        TypedQuery<Student> like = em.createQuery("select se from Student se where se.adress.city like ?1", Student.class);
        like.setParameter(1,"Timisoara");
        Student singleResult = like.getSingleResult();
        Assert.assertEquals("Query did not return the correct result!", "Timisoara", singleResult.getAdress().getCity());



    }

    @Test
    public void testCertainSubject() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException {
        TypedQuery<Subject> like = em.createQuery("select se from Subject se where se.name like ?1", Subject.class);
        like.setParameter(1, "Math");
        List<Subject> list = like.getResultList();
        Assert.assertEquals(1, list.size());

    }

    @Test
    public void calculateAverage(){

        TypedQuery<Double> avgQuery = em.createQuery("select avg(grade.value) from Student student JOIN student.grades grade group by student.first_name", Double.class);
        for (Double dd : avgQuery.getResultList())
            System.out.println(dd);
    }

    @Test
    public void aboveAverage(){

        TypedQuery<Student> avgAboveQuery = em.createQuery("select student from Student student JOIN student.grades grade where grade.value>8 ",Student.class);
        List<Student> list = avgAboveQuery.getResultList();
        System.out.println(list.get(0).getFirst_name());
        System.out.println(list.get(1).getFirst_name());
        Assert.assertEquals("Query did not return the correct result!", 2, avgAboveQuery.getResultList().size());
    }

    @Test
    public void testCity(){

        Query countQuery = em.createQuery("select count(adres.city) from Student student JOIN student.adress adres group by adres.city");
        Assert.assertEquals(Long.valueOf(1),countQuery.getSingleResult());
    }


    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }


    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();

        List<Student> studList = new ArrayList<>();
        List<Grades> gradeList = new ArrayList<>();

        Student s1 = new Student();
        Student s2 = new Student();

        Subject sub1 = new Subject();
        Subject sub2 = new Subject();

        Adress a1 = new Adress();
        Adress a2 = new Adress();

        Grades g1= new Grades();
        Grades g2 = new Grades();

        g1.setValue(5);
        g2.setValue(9);

        gradeList.add(g1);
        gradeList.add(g2);

        a1.setCountry("Romania");
        a1.setCity("Timisoara");
        a1.setStreet("Princ");

        a2.setCity("Buc");
        a2.setStreet("Sec");
        a2.setCountry("Rom");

        s1.setFirst_name("Ion");
        s1.setLast_name("Gh");
        s1.setAdress(a1);
        s1.setGrades(gradeList);

        s2.setLast_name("In");
        s2.setFirst_name("Gheorghe");
        s2.setAdress(a2);
        s2.setGrades(gradeList);


        studList.add(s1);
        studList.add(s2);

        sub1.setName("Math");
        sub1.setManyToManyStudent(studList);
        sub1.setGrade(g1);

        sub2.setName("English");
        sub2.setManyToManyStudent(studList);
        sub2.setGrade(g2);

        for(Grades grad : gradeList)
            em.persist(grad);

        for(Student stud : studList)
            em.persist(stud);

        em.persist(sub1);
        em.persist(sub2);

        utx.commit();
    }

    @Override
    protected void internalClearData() {

        em.createQuery("delete from Student").executeUpdate();
        em.createQuery("delete from Subject").executeUpdate();
        em.createQuery("delete from Grades").executeUpdate();


    }
}
