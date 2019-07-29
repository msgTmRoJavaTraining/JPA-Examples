package group.msg.test.jpa;

import group.msg.examples.jpa.entity.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class MyJPQLTest extends JPABaseTest {

    @Inject
    Logger logger;

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
        List<StudentEntity> students = new ArrayList<>();

        UniversityEntity univ1 = new UniversityEntity();
        univ1.setName("UPT");
        univ1.setCountry("Romania");

        UniversityEntity univ2 = new UniversityEntity();
        univ2.setName("UVT");
        univ2.setCountry("Romania");

        StudentEntity stud1 = new StudentEntity();
        stud1.setFirst_name("Popescu");
        stud1.setLast_name("Ion");

        StudentEntity stud2 = new StudentEntity();
        stud2.setFirst_name("Andreescu");
        stud2.setLast_name("Mihai");

        EmbeddableAddressEntity address1 = new EmbeddableAddressEntity();
        address1.setCountry("Romania");
        address1.setCity("Timisoara");
        address1.setStreet("Torontalului");
        stud1.setAddress(address1);
        stud1.setUniversity(univ1);

        EmbeddableAddressEntity address2 = new EmbeddableAddressEntity();
        address2.setCountry("Romania");
        address2.setCity("Timisoara");
        address2.setStreet("Hector");
        stud2.setAddress(address2);
        stud2.setUniversity(univ2);

        SubjectEntity subj1 = new SubjectEntity();
        subj1.setName("Mathematics");
        students.add(stud1);
        subj1.setStudents(students);

        GradeEntity grade1 = new GradeEntity();
        grade1.setGrade(9);
        grade1.setSubject(subj1);
        grade1.setStudent(stud1);

        GradeEntity grade2 = new GradeEntity();
        grade2.setGrade(6);
        grade2.setSubject(subj1);
        grade2.setStudent(stud1);

        em.persist(univ1);
        em.persist(univ2);
        em.persist(subj1);
        em.persist(stud1);
        em.persist(stud2);
        em.persist(grade1);
        em.persist(grade2);

        utx.commit();
        em.clear();
    }

    @Test
    public void allStudentsFromCity() {
        TypedQuery<StudentEntity> jpql = em.createQuery("select s from StudentEntity s where s.address.city= ?1", StudentEntity.class);
        String city = "Timisoara";
        jpql.setParameter(1, city);
        List<StudentEntity> result = jpql.getResultList();
        logger.info("Students from " + city + ":");
        for (StudentEntity s : result
        ) {
            logger.info(s.getFirst_name() + " " + s.getLast_name());
        }
    }

    @Test
    public void allStudentsWithSubject() {
        TypedQuery<StudentEntity> jpql = em.createQuery("select s from StudentEntity s join s.subjects subj where subj.name=?1", StudentEntity.class);
        String subject = "Mathematics";
        jpql.setParameter(1, subject);
        List<StudentEntity> list = jpql.getResultList();
        Assert.assertEquals("Failed test!", 1, jpql.getResultList().size());
        for (StudentEntity s : list
        ) {
            logger.info(subject + s.getFirst_name() + " " + s.getLast_name());
        }
    }

    @Test
    public void averageGrade() {
        TypedQuery<StudentEntity> jpql = em.createQuery("select avg(gr.grade) from StudentEntity s join s.grades gr", StudentEntity.class);
        Assert.assertEquals("Failed test!", 1, jpql.getResultList().size());
        logger.info("Result= " + jpql.getResultList().toString());
    }

    @Test
    public void averageGradeAbove8() {
        TypedQuery<StudentEntity> jpql = em.createQuery("select avg(gr.grade) from StudentEntity s join s.grades gr having avg(gr.grade) > 7 ", StudentEntity.class);
        Assert.assertEquals("Failed test!", 1, jpql.getResultList().size());
        logger.info("Result= " + jpql.getResultList().toString());
    }

    @Test
    public void studentsFromEveryCity() {
        Query jpql = em.createQuery("select count(s.student_id),s.address.city from StudentEntity s group by s.address.city order by count(s.student_id)");
        List results = jpql.getResultList();
        logger.info("Total City");
        for (Object s : results) {
            List<Object> list = Arrays.asList((Object[]) s);
            logger.info(list.get(0).toString() + " " + list.get(1).toString());
        }
    }

    @Override
    protected void internalClearData() {
        em.createNativeQuery("delete from GRADES").executeUpdate();
        em.createNativeQuery("delete from STUDENT_SUBJECT").executeUpdate();
        em.createNativeQuery("delete from SubjectEntity").executeUpdate();
        em.createNativeQuery("delete from Students").executeUpdate();
        em.createNativeQuery("delete from Universities").executeUpdate();
    }
}
