package group.msg.test.jpa.jpql;

import group.msg.examples.jpa.entity.*;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@RunWith(Arquillian.class)
public class JpqlTest extends JPABaseTest {

    private static List<Object> persistedObjects = new ArrayList<>();
    private List<Subject> materii = new ArrayList<>();
    private List<Subject> materii2 = new ArrayList<>();
    private List<Grades> note = new ArrayList<>();

    @Test
    public void testCity() {

        TypedQuery<StudentEntity> like = em.createQuery("select i from StudentEntity i where i.homeAddress.city=:name", StudentEntity.class);
        like.setParameter("name", "Arad");
        Assert.assertEquals("Query did not return the correct result!", 2, like.getResultList().size());
    }

    @Test
    public void testSubject() {
        TypedQuery<StudentEntity> like = em.createQuery("select se from StudentEntity se join fetch se.subjects n where n.name=:name", StudentEntity.class);
        like.setParameter("name", "chimie");
        Assert.assertEquals("Query did not return the correct result!", 3, like.getResultList().size());
    }

    @Test
    public void testGrade() {

        Query groupBy = em.createQuery("select avg(nota.valoare) from StudentEntity student join fetch student.grades nota group by student.first_name");
        System.out.println(groupBy.getResultList().toString());

    }

    @Test
    public void testAboveGrade() {

        Query groupBy = em.createQuery("select avg(n.valoare) from StudentEntity student join fetch student.grades n group by student.first_name having avg(n.valoare)>8");

        List<Double> list = groupBy.getResultList();
        for (Double d : list)
            System.out.println(d + " ");
    }

    @Test
    public void testNumberOfStudentsLocated() {

        Query groupBy = em.createQuery("select count(student.homeAddress.city) from StudentEntity student group by student.homeAddress.city");

        List<Long> list = groupBy.getResultList();

        for (long i : list)
            System.out.println(i + " ");

    }

    @Test
    public void testCityApi() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();

        Root<StudentEntity> stud = query.from(em.getMetamodel().entity(StudentEntity.class));
        Join<StudentEntity, HomeAddress> joinAddress = stud.join("homeAddress");

        TypedQuery<StudentEntity> result = em.createQuery(query.select(stud).where(builder.equal(joinAddress.get("city"), "Arad")));

        List<StudentEntity> list = result.getResultList();
        list.forEach(e -> System.out.println(e.getFirst_name() + " from : " + e.getHomeAddress().getCity()));

        Assert.assertEquals("Query did not return the correct result!", 2, result.getResultList().size());
    }


    @Test
    public void testSubjectApi() {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();

        Root<StudentEntity> stud = query.from(em.getMetamodel().entity(StudentEntity.class));
        Join<StudentEntity, Subject> joinSubject = stud.join("subjects");

        TypedQuery<StudentEntity> result = em.createQuery(query.select(stud).where(builder.equal(joinSubject.get("name"), "chimie")));

        List<StudentEntity> list = result.getResultList();
        list.forEach(e -> System.out.println(e.getFirst_name()));

        Assert.assertEquals("Query did not return the correct result!", 3, result.getResultList().size());
    }

    @Test
    public void testGradeApi() {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Double.class);

        Root<StudentEntity> stud = query.from(em.getMetamodel().entity(StudentEntity.class));
        Join<StudentEntity, Grades> joinGrades = stud.join("grades");

        query.select(builder.avg(joinGrades.get(Grades_.valoare))).groupBy(stud.get(StudentEntity_.first_name));

        TypedQuery<Double> nume = em.createQuery(query);
        List<Double> list = nume.getResultList();
        list.forEach(System.out::println);

        Assert.assertEquals("Query did not return the correct result!", 2, nume.getResultList().size());
    }

    @Test
    public void testGradesAboveApi() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Double.class);

        Root<StudentEntity> stud = query.from(em.getMetamodel().entity(StudentEntity.class));
        Join<StudentEntity, Grades> joinGrades = stud.join("grades");

        query.select(builder.avg(joinGrades.get(Grades_.valoare))).groupBy(stud.get(StudentEntity_.first_name))

                .having(builder.gt(builder.avg(joinGrades.get(Grades_.valoare)), 8));

        TypedQuery<Double> nume = em.createQuery(query);
        List<Double> list = nume.getResultList();
        list.forEach(System.out::println);
        Assert.assertEquals("Query did not return the correct result!", 1, nume.getResultList().size());
    }

    @Test
    public void testNumberOfStudentsLocatedApi() {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Double.class);

        Root<StudentEntity> stud = query.from(em.getMetamodel().entity(StudentEntity.class));
        Join<StudentEntity, HomeAddress> joinAddress = stud.join("homeAddress");

        query.select(builder.count(joinAddress.get(HomeAddress_.city))).groupBy(joinAddress.get(HomeAddress_.city));
        TypedQuery<Long> nr = em.createQuery(query);

        List<Long> list = nr.getResultList();
        list.forEach(System.out::println);

    }


    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();

        Subject fizica = new Subject();
        fizica.setName("fizica");

        Subject chimie = new Subject();
        chimie.setName("chimie");

        Subject mate = new Subject();
        mate.setName("matematica");

        materii.add(fizica);
        materii.add(chimie);
        materii.add(mate);

        materii2.add(mate);
        materii2.add(fizica);

        for (Subject s : materii)
            em.persist(s);

        for (Subject s : materii2)
            em.persist(s);

        StudentEntity student = new StudentEntity();
        student.setFirst_name("Andrei");
        student.setHomeAddress(new HomeAddress("Arges", "Arad", "Romania"));
        student.setSubjects(materii);
        student.setGrades(note);
        em.persist(student);
        persistedObjects.add(student);


        StudentEntity student1 = new StudentEntity();
        student1.setFirst_name("Ionut");
        student1.setHomeAddress(new HomeAddress("Arges", "Timisoara", "Romania"));
        student1.setSubjects(materii);
        student1.setGrades(note);
        em.persist(student1);
        persistedObjects.add(student1);


        StudentEntity student3 = new StudentEntity();
        student3.setFirst_name("Ioana");
        student3.setHomeAddress(new HomeAddress("Aradi", "Mako", "Ungaria"));
        student3.setSubjects(materii);
        em.persist(student3);
        persistedObjects.add(student3);


        StudentEntity student4 = new StudentEntity();
        student4.setFirst_name("Jhon");
        student4.setHomeAddress(new HomeAddress("Trenului", "Arad", "China"));
        student4.setSubjects(materii2);
        em.persist(student4);
        persistedObjects.add(student4);

        StudentEntity student5 = new StudentEntity();
        student5.setFirst_name("Larisa");
        student5.setHomeAddress(new HomeAddress("Trenului", "Timisoara", "Iran"));
        student5.setSubjects(materii2);

        em.persist(student5);
        persistedObjects.add(student5);


        Grades g2 = new Grades();
        g2.setValoare(10);
        g2.setStudentEntity(student);
        g2.setSubject(mate);

        Grades g3 = new Grades();
        g3.setValoare(10);
        g3.setStudentEntity(student);
        g3.setSubject(fizica);

        Grades g4 = new Grades();
        g4.setValoare(7);
        g4.setStudentEntity(student);
        g4.setSubject(chimie);

        Grades g5 = new Grades();
        g5.setValoare(7.1);
        g5.setStudentEntity(student1);
        g5.setSubject(mate);


        note.add(g2);
        note.add(g3);
        note.add(g4);
        note.add(g5);
        for (Grades g : note)
            em.persist(g);

        utx.commit();
    }

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Override
    protected void internalClearData() {

        for (Object persisted : persistedObjects) {
            Object entity = em.merge(persisted);
            em.remove(entity);
        }
    }
}
