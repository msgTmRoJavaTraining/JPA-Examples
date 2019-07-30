package group.msg.test.jpa.day16;

import group.msg.examples.jpa.entity.day14.*;
import group.msg.test.jpa.JPABaseTest;
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
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class TestCasesCurrentDay extends JPABaseTest {
    @Inject
    private Logger logger;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    // All the students that are located in a certain city
    @Test
    public void firstTest() {
        TypedQuery<StudentEntity> query = em.createQuery("select stud.first_name from StudentEntity stud where stud.home_address.city = :givenCity", StudentEntity.class);
        query.setParameter("givenCity", "Timisoara");

        Assert.assertEquals("First Query", 2, query.getResultList().size());
        System.out.println("Query: " + query.getResultList().toString());
    }

    //All the students that have a certain subject associated
    @Test
    public void secondTest() {
        Query query = em.createQuery("select stud.first_name, stud.last_name, sub.name from StudentEntity stud join fetch stud.materiiStudent sub where sub.name = :givenSubject");
        query.setParameter("givenSubject", "Programare Java");

        List response = query.getResultList();

        Assert.assertEquals("Second Query", 2, response.size());
        showQueryData(response);
    }

    //Calculated average grade for each student
    @Test
    public void thirdTest() {
        Query query = em.createQuery("select stud.student_id, stud.last_name, stud.first_name, avg(grade.grade) from StudentEntity stud join fetch stud.grades grade group by stud.student_id, stud.last_name, stud.first_name");

        List response = query.getResultList();
        showQueryData(response);
    }

    //All the students with the average grade above 8
    @Test
    public void fourthTest() {
        Query query = em.createQuery("select stud.student_id, stud.last_name, stud.first_name, avg(grade.grade) as medie from StudentEntity stud join fetch stud.grades grade group by stud.student_id, stud.last_name, stud.first_name having medie >= 8");

        List response = query.getResultList();
        showQueryData(response);
    }

    //Count how many students are located for each city found in address
    @Test
    public void fifthTest() {
        Query query = em.createQuery("select count(stud.student_id) as number, stud.home_address.city as city from StudentEntity stud group by city");

        List response = query.getResultList();
        showQueryData(response);
    }

    //Show Query result
    private void showQueryData(List response) {
        StringBuilder finalMessage = new StringBuilder();

        response.forEach(r -> {
            if (r instanceof Object[]) {
                List s = Arrays.asList((Object[]) r);

                s.forEach(item -> {
                    finalMessage.append(item.toString());
                    finalMessage.append(" ");
                });

                finalMessage.append("\n");
            }
        });

        logger.info("Query Result:\n" + finalMessage);
    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");

        // *** Universitatea ***
        UniversityEntity university = new UniversityEntity();
        university.setName("Universitatea Politehnica Timisoara");
        university.setCountry("Romania");

        // *** Lista Adrese ***
        List<AddressEntity> addresses = new ArrayList<>();
        addresses.add(new AddressEntity());
        addresses.add(new AddressEntity());

        addresses.get(0).setCity("Timisoara");
        addresses.get(0).setStreet("Aleea Studentilor Camin 11C");
        addresses.get(0).setCountry("Romania");

        addresses.get(1).setCity("Timisoara");
        addresses.get(1).setStreet("Aleea Studentilor Camin 14C");
        addresses.get(1).setCountry("Romania");

        // *** Lista Studenti ***
        List<StudentEntity> students = new ArrayList<>();
        StudentEntity student1 = new StudentEntity();
        StudentEntity student2 = new StudentEntity();

        student1.setStudent_id(50);
        student1.setFirst_name("Andrei");
        student1.setLast_name("Astanei");
        student1.setHome_address(addresses.get(0));
        student1.setUniversity_id(university);
        student1.setSection("Informatica");
        student1.setEmail("andreihao@gmail.com");

        student2.setStudent_id(51);
        student2.setFirst_name("Mihai");
        student2.setLast_name("Caravan");
        student2.setHome_address(addresses.get(1));
        student2.setUniversity_id(university);
        student2.setSection("Ingineria Sistemelor");
        student2.setEmail("mihaicaravan@yahoo.com");

        // *** Lista Materii ***
        List<SubjectEntity> subjects = new ArrayList<>();
        subjects.add(new SubjectEntity());
        subjects.add(new SubjectEntity());
        subjects.get(0).setName("Programare Java");
        subjects.get(1).setName("Programarea Aplicatiilor Multimedia");

        List<GradesEntity> grades = new ArrayList<>();
        grades.add(new GradesEntity());
        grades.add(new GradesEntity());
        grades.add(new GradesEntity());
        grades.add(new GradesEntity());

        grades.get(0).setGrade(10);
        grades.get(0).setSubject(subjects.get(0));
        grades.get(0).setStudent(student1);

        grades.get(2).setGrade(8);
        grades.get(2).setSubject(subjects.get(1));
        grades.get(2).setStudent(student1);

        grades.get(1).setGrade(9);
        grades.get(1).setSubject(subjects.get(0));
        grades.get(1).setStudent(student2);

        grades.get(3).setGrade(5);
        grades.get(3).setSubject(subjects.get(1));
        grades.get(3).setStudent(student2);

        subjects.get(0).setGrade(grades.get(0));
        subjects.get(0).setGrade(grades.get(1));
        subjects.get(1).setGrade(grades.get(2));
        subjects.get(1).setGrade(grades.get(3));

        student1.setMateriiStudent(Arrays.asList(subjects.get(0), subjects.get(1)));
        student2.setMateriiStudent(Arrays.asList(subjects.get(1), subjects.get(0)));

        students.add(student1);
        students.add(student2);

        try {
            em.persist(university);

            for (SubjectEntity subjectEntity : subjects) {
                em.persist(subjectEntity);
            }

            for (StudentEntity s : students) {
                em.persist(s);
            }

            for (GradesEntity g : grades) {
                em.persist(g);
            }

            utx.commit();
        } catch (ConstraintViolationException e) {
            utx.rollback();
            ConstraintViolation<?> constraint = e.getConstraintViolations().iterator().next();
            String nameValidationMessage = constraint.getMessage();

            logger.info("Mesajul de eroare la validarea numelui este: " + nameValidationMessage);
        } catch (HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            e.printStackTrace();
        }

        em.clear();
    }

    @Override
    protected void internalClearData() {
        em.createQuery("delete from StudentEntity").executeUpdate();
    }
}
