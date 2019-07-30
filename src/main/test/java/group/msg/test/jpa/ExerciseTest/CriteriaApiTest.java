package group.msg.test.jpa.ExerciseTest;

import group.msg.exercises.entities.*;
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
import javax.persistence.criteria.*;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.util.ArrayList;
import java.util.List;

@RunWith(Arquillian.class)
public class CriteriaApiTest extends JPABaseTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testCertainAdress() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<Student> studentRoot = query.from(em.getMetamodel().entity(Student.class));
        Join<Student, Adress> adressJoin = studentRoot.join("adress");

        Predicate condition = builder.equal(adressJoin.get("city"), "Timisoara");
        TypedQuery<Student> studentTypedQuery = em.createQuery(query.select(studentRoot).where(condition));
        Assert.assertEquals(1, studentTypedQuery.getResultList().size());


    }

    @Test
    public void testCertainSubject() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException {


        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<Student> studentRoot = query.from(em.getMetamodel().entity(Student.class));
        Join<Student, Adress> subjectJoin = studentRoot.join("manyToManySubject");

        Predicate condition = builder.equal(subjectJoin.get("name"), "English");
        TypedQuery<Student> studentQuery = em.createQuery(query.select(studentRoot).where(condition));
        Assert.assertEquals(2, studentQuery.getResultList().size());
    }

    @Test
    public void calculateAverage() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<Student> studentRoot = query.from(em.getMetamodel().entity(Student.class));
        Join<Student, Grades> gradesJoin = studentRoot.join("grades");

        query.select(builder.avg(gradesJoin.get("value"))).groupBy(studentRoot.get(Student_.first_name));

        TypedQuery<Double> query1 = em.createQuery(query);
        for (Double s1 : query1.getResultList())
            System.out.println(s1);


    }

    @Test
    public void aboveAverage() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<Student> studentRoot = query.from(em.getMetamodel().entity(Student.class));
        Join<Student, Grades> gradesJoin = studentRoot.join("grades");
        Predicate condition = builder.greaterThan(builder.avg(gradesJoin.get("value")), 8.0);

        query.select(studentRoot.get("first_name")).having(condition).groupBy(studentRoot.get(Student_.first_name));
        TypedQuery<Student> typedAverageQuery = em.createQuery(query);
        for (Student stud : typedAverageQuery.getResultList())
            System.out.println(stud.getFirst_name());
    }

    @Test
    public void testCity() {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<Student> studentRoot = query.from(em.getMetamodel().entity(Student.class));
        Join<Student, Adress> adressJoin = studentRoot.join("adress");

        query.select(builder.count(adressJoin.get("city"))).groupBy(adressJoin.get("city"));
        TypedQuery<Integer> typedAdressQuery = em.createQuery(query);
        System.out.println(typedAdressQuery.getResultList().get(0));

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

        Grades g1 = new Grades();
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

        for (Grades grad : gradeList)
            em.persist(grad);

        for (Student stud : studList)
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
