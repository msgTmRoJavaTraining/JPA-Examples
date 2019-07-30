package group.msg.test.jpa.day17;

import group.msg.examples.jpa.entity.day14.*;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.criteria.*;
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
public class TestCasesDay17 extends JPABaseTest {
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
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<StudentEntity> studentEntityRoot = query.from(em.getMetamodel().entity(StudentEntity.class));

        //https://www.objectdb.com/java/jpa/query/jpql/select , https://stackoverflow.com/questions/29023182/using-an-embeddable-entity-in-a-jpa-criteriaquery
        query.multiselect(studentEntityRoot.get(StudentEntity_.first_name),
                studentEntityRoot.get(StudentEntity_.last_name),
                studentEntityRoot.get(StudentEntity_.home_address).get(AddressEntity_.city));

        query.where(builder.equal(
                studentEntityRoot.get(StudentEntity_.home_address).get(AddressEntity_.city),
                "Timisoara"
        ));

        List<Object[]> results = em.createQuery(query).getResultList();
        for (Object[] result : results) {
            System.out.println("Student: " + result[0] + " " + result[1] + ", " + result[2]);
        }
    }

    ////All the students that have a certain subject associated
    @Test
    public void secondTest() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<StudentEntity> studentEntityRoot = query.from(em.getMetamodel().entity(StudentEntity.class));

        Join<StudentEntity, SubjectEntity> fetch = (Join<StudentEntity, SubjectEntity>) studentEntityRoot.fetch(StudentEntity_.materiiStudent, JoinType.INNER);

        query.multiselect(studentEntityRoot.get(StudentEntity_.last_name),
                studentEntityRoot.get(StudentEntity_.first_name),
                fetch.get(SubjectEntity_.name));

        query.where(builder.equal(
                fetch.get(SubjectEntity_.name),
                "Programare Java"
        ));

        List<Object[]> results = em.createQuery(query).getResultList();
        for (Object[] result : results) {
            System.out.println("Student: " + result[0] + " " + result[1] + ", materia: " + result[2]);
        }
    }

    //Calculated average grade for each student
    @Test
    public void thirdTest() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<StudentEntity> studentEntityRoot = query.from(em.getMetamodel().entity(StudentEntity.class));

        Join<StudentEntity, GradesEntity> fetch = (Join<StudentEntity, GradesEntity>) studentEntityRoot.fetch(StudentEntity_.grades, JoinType.INNER);

        query.multiselect(studentEntityRoot.get(StudentEntity_.last_name),
                studentEntityRoot.get(StudentEntity_.first_name),
                builder.avg(fetch.get(GradesEntity_.grade)));

        query.groupBy(studentEntityRoot.get(StudentEntity_.last_name), studentEntityRoot.get(StudentEntity_.first_name));

        List<Object[]> results = em.createQuery(query).getResultList();
        for (Object[] result : results) {
            System.out.println("Student: " + result[0] + " " + result[1] + ", media: " + result[2]);
        }
    }

    //All the students with the average grade above 8
    @Test
    public void fourthTest() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<StudentEntity> studentEntityRoot = query.from(em.getMetamodel().entity(StudentEntity.class));

        Join<StudentEntity, GradesEntity> fetch = (Join<StudentEntity, GradesEntity>) studentEntityRoot.fetch(StudentEntity_.grades, JoinType.INNER);

        query.multiselect(studentEntityRoot.get(StudentEntity_.last_name),
                studentEntityRoot.get(StudentEntity_.first_name),
                builder.avg(fetch.get(GradesEntity_.grade)));

        query.having(builder.greaterThan(
                builder.avg(fetch.get(GradesEntity_.grade)),
                8.0
        ));

        query.groupBy(studentEntityRoot.get(StudentEntity_.last_name), studentEntityRoot.get(StudentEntity_.first_name));

        List<Object[]> results = em.createQuery(query).getResultList();
        for (Object[] result : results) {
            System.out.println("Student: " + result[0] + " " + result[1] + ", media: " + result[2]);
        }
    }

    //Count how many students are located for each city found in address
    @Test
    public void fifthTest() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root<StudentEntity> studentEntityRoot = query.from(em.getMetamodel().entity(StudentEntity.class));

        query.multiselect(studentEntityRoot.get(StudentEntity_.home_address).get(AddressEntity_.city),
                builder.count(studentEntityRoot.get(StudentEntity_.student_id)));

        query.groupBy(studentEntityRoot.get(StudentEntity_.home_address).get(AddressEntity_.city));

        List<Object[]> results = em.createQuery(query).getResultList();
        for (Object[] result : results) {
            System.out.println("Studenti in " + result[0] + " : " + result[1]);
        }
    }

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");

        // *** Universitatea ***
        UniversityEntity university = createNewUniversity("Universitatea Politehnica Timisoara", "Romania");

        // *** Adrese ***
        AddressEntity address1 = createNewAddress("Timisoara", "Aleea Studentilor Camin 11C", "Romania");
        AddressEntity address2 = createNewAddress("Timisoara", "Aleea Studentilor Camin 14C", "Romania");

        // *** Lista Studenti ***
        List<StudentEntity> students = new ArrayList<>();
        StudentEntity student1 = createNewStudent("Astanei", "Andrei", address1, university, "Informatica", "andreihao@gmail.com");
        StudentEntity student2 = createNewStudent("Caravan", "Mihai", address2, university, "Ingineria Sistemelor", "mihaicaravan@yahoo.com");

        // *** Lista Materii ***
        List<SubjectEntity> subjects = new ArrayList<>();
        SubjectEntity subject1 = createNewSubject("Programare Java");
        SubjectEntity subject2 = createNewSubject("Programarea Aplicatiilor Multimedia");

        // *** Lista Note ***
        List<GradesEntity> grades = new ArrayList<>();
        GradesEntity grade1 = createNewGrade(10, subject1, student1);
        GradesEntity grade2 = createNewGrade(8, subject2, student1);
        GradesEntity grade3 = createNewGrade(9, subject1, student2);
        GradesEntity grade4 = createNewGrade(5, subject2, student2);

        // *** Setare Note la Materii ***
        subject1.setGrade(grade1);
        subject1.setGrade(grade2);
        subject2.setGrade(grade3);
        subject2.setGrade(grade4);

        // *** Setare Materii la Studenti ***
        student1.setMateriiStudent(Arrays.asList(subject1, subject2));
        student2.setMateriiStudent(Arrays.asList(subject2, subject1));

        // *** Adaugare valori in liste ***
        students.add(student1);
        students.add(student2);

        subjects.add(subject1);
        subjects.add(subject2);

        grades.add(grade1);
        grades.add(grade2);
        grades.add(grade3);
        grades.add(grade4);

        // *** Persistare Date in Baza de Date ***
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

    //Functie pentru crearea unui nou student
    private StudentEntity createNewStudent(String lastName, String firstName, AddressEntity studentAddress, UniversityEntity studentUniversity, String universitySection, String studentMail) {
        StudentEntity student = new StudentEntity();

        student.setLast_name(lastName);
        student.setFirst_name(firstName);
        student.setHome_address(studentAddress);
        student.setUniversity_id(studentUniversity);
        student.setSection(universitySection);
        student.setEmail(studentMail);

        return student;
    }

    //Functie pentru crearea unei noi universitati
    private UniversityEntity createNewUniversity(String universityName, String universityCountry) {
        UniversityEntity university = new UniversityEntity();
        university.setName(universityName);
        university.setCountry(universityCountry);

        return university;
    }

    //Functie pentru crearea unei noi adrese
    private AddressEntity createNewAddress(String addressCity, String addressStreet, String addressCountry) {
        AddressEntity address = new AddressEntity();
        address.setCity(addressCity);
        address.setStreet(addressStreet);
        address.setCountry(addressCountry);

        return address;
    }

    //Functie pentru crearea unei noi materii
    private SubjectEntity createNewSubject(String subjectTitle) {
        SubjectEntity subject = new SubjectEntity();
        subject.setName(subjectTitle);

        return subject;
    }

    //Functie pentru crearea unei noi note
    private GradesEntity createNewGrade(int gradeValue, SubjectEntity gradeSubject, StudentEntity gradeStudent) {
        GradesEntity grade = new GradesEntity();
        grade.setGrade(gradeValue);
        grade.setSubject(gradeSubject);
        grade.setStudent(gradeStudent);

        return grade;
    }
}
