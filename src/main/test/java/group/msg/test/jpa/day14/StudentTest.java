package group.msg.test.jpa.day14;


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
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class StudentTest extends JPABaseTest {

    @Inject
    private Logger logger;

    @PersistenceUnit(unitName = "test")
    private EntityManagerFactory emf;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }


    @Test
    public void studentsInACity() {
        TypedQuery<Student> location = em.createQuery("select se.first_name from Student se where se.address.city like 'Timisoara'", Student.class);
        Assert.assertEquals("Entities not found in the database!", 2, location.getResultList().size());
        System.out.println("Students that live in this city are: " + location.getResultList().toString());
    }

    @Test
    public void studentsCertainSubject() {
        TypedQuery<Student> subject = em.createQuery("select distinct stud.first_name from StudentEntity stud join fetch stud.subjectList sub where sub.name='Mate'", Student.class);
        System.out.println("Students with this subject are: " + subject.getResultList().toString());
    }

    @Test
    public void averageGrade() {
        TypedQuery<Student> average = em.createQuery("select stud.first_name, avg(grade.grade) from StudentEntity stud join fetch stud.grade grade group by stud.first_name", Student.class);

        List response = average.getResultList();
        response.forEach(r -> {
            if (r instanceof Object[]) {
                List s = Arrays.asList((Object[]) r);
                logger.info(s.get(0).toString() + "'s average is: " + s.get(1).toString());
            }
        });
    }

    @Test
    public void averageGradeAboveEight() {
        TypedQuery<Student> average = em.createQuery("select stud.first_name, avg(grade.grade) from StudentEntity stud join fetch stud.grade grade group by stud.first_name having avg(grade.grade) > 8", Student.class);

        List response = average.getResultList();
        response.forEach(r -> {
            if (r instanceof Object[]) {
                List s = Arrays.asList((Object[]) r);
                logger.info(s.get(0).toString() + "'s average is: " + s.get(1).toString());
            }
        });
    }

    @Test
    public void countStudentsInCity() {
        TypedQuery<Student> count = em.createQuery("select  stud.address.city, count(stud.first_name) from StudentEntity stud group by stud.address.city", Student.class);

        List response = count.getResultList();
        response.forEach(r -> {
            if (r instanceof Object[]) {
                List s = Arrays.asList((Object[]) r);
                logger.info(s.get(0).toString() + " has " + s.get(1).toString() + " students ");
            }
        });
    }

    /****************************************************/


    @Test
    public void studentsInACityAPI() {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Student.class);

        Root<Student> studentEntity = query.from(em.getMetamodel().entity(Student.class));

        Join<Student, Address> address = studentEntity.join(Student_.address);

        CriteriaQuery studentSelect = query.select(studentEntity.get(Student_.first_name));
        studentSelect.where(builder.like(address.get("city"), "Timisoara"));

        List<Object> result = em.createQuery(query).getResultList();

        for (Object s : result) {
            System.out.println("Students that live in this city: " + s);
        }
    }

    @Test
    public void studentsCertainSubjectAPI() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Student.class);

        Root<Student> studentEntity = query.from(em.getMetamodel().entity(Student.class));

        Join<Student, Subject> subject = studentEntity.join(Student_.subjectList);

        CriteriaQuery studentSelect = query.select(studentEntity.get(Student_.first_name));
        studentSelect.where(builder.like(subject.get("name"), "Mate"));

        List<Object> result = em.createQuery(query).getResultList();

        for (Object s : result) {
            System.out.println("Students with this subject are: " + s);
        }
    }

    @Test
    public void averageGradeAPI() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);

        Root<Student> studentEntity = query.from(em.getMetamodel().entity(Student.class));

        Join<Student, Grade> grade = (Join<Student, Grade>) studentEntity.fetch(Student_.grade);

        query.multiselect(studentEntity.get(Student_.first_name), builder.avg(grade.get("grade"))).groupBy(studentEntity.get(Student_.first_name));

        List<Object[]> result = em.createQuery(query).getResultList();
        for (Object[] s : result) {
            System.out.println(s[0] + "'s average is " + s[1]);
        }
    }

    @Test
    public void averageAboveEightAPI() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();

        Root<Student> studentEntity = query.from(em.getMetamodel().entity(Student.class));

        Join<Student, Grade> grade = (Join<Student, Grade>) studentEntity.fetch(Student_.grade);

        query.multiselect(studentEntity.get(Student_.first_name), builder.avg(grade.get(Grade_.grade))).having(builder.greaterThan(builder.avg(grade.get(Grade_.grade)), 8.0)).groupBy(studentEntity.get(Student_.first_name));

        List<Object[]> result = em.createQuery(query).getResultList();
        for (Object[] s : result) {
            System.out.println(s[0] + "'s average is " + s[1]);
        }
    }

    @Test
    public void countStudentsAPI() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();

        Root<Student> studentEntity = query.from(em.getMetamodel().entity(Student.class));

        Join<Student, Address> address = (Join<Student, Address>) studentEntity.fetch(Student_.address);

        query.multiselect(address.get(Address_.city), builder.count(studentEntity.get(Student_.first_name))).groupBy(address.get(Address_.city));

        List<Object[]> result = em.createQuery(query).getResultList();
        for (Object[] s : result) {
            System.out.println(s[0].toString() + " has " + s[1].toString() + " students ");
        }
    }

    /*****************************************************/

    @Test
    public void createStudentEntity() {
        Query q = em.createNativeQuery("SELECT * FROM STUDENT");
        Assert.assertEquals("Entities not found in the database!", 3, q.getResultList().size());
    }

    @Test
    public void findStudentEntity() {
        Query q = em.createNativeQuery("SELECT * FROM STUDENT WHERE FIRST_NAME ='Larisa'");
        Assert.assertEquals("Entities not found in the database!", 1, q.getResultList().size());
    }

    @Test
    public void deleteStudentEntity() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        utx.begin();
        em.joinTransaction();
        Student student = em.find(Student.class, 50);
        em.remove(student);
        utx.commit();

        em.clear();
    }

    /*********************************************************/

    @Test
    public void validateName() throws Exception {
        utx.begin();
        em.joinTransaction();
        Address address = createAddress("Romania", "Lunga", "Timisoara");

        University university = createUniversity("Romania", "Politehnica");
        em.persist(university);

        List<Subject> subjects = new ArrayList<>();
        Subject subject = createSubject("Romana");
        subjects.add(subject);

        for (Subject sub : subjects) {
            em.persist(sub);
        }

        Student s = createStudent(address, "alex.gheorghe@gmail.com", "Alex", "Gheorghe", university, "Section1", subjects);

        try {
            em.persist(s);
            utx.commit();
        } catch (ConstraintViolationException e) {
            utx.rollback();
            ConstraintViolation<?> constraint = e.getConstraintViolations().iterator().next();
            String message = constraint.getMessage();
            System.out.println("Invalid constraint violation for field: " + message);
        }
    }

    @Test
    public void validateAddress() throws Exception {
        utx.begin();
        em.joinTransaction();
        Address address = createAddress("Romania", "Lunga", "Timisoara");

        University university = createUniversity("Romania", "Politehnica");
        em.persist(university);

        List<Subject> subjects = new ArrayList<>();
        Subject subject = createSubject("Romana");
        subjects.add(subject);

        for (Subject sub : subjects) {
            em.persist(sub);
        }
        Student s = createStudent(address, "alex.gheorghe@gmail.com", "Alex", "Gheorghe", university, "Section1", subjects);


        try {
            em.persist(s);
            utx.commit();
        } catch (ConstraintViolationException e) {
            utx.rollback();
            ConstraintViolation<?> constraint = e.getConstraintViolations().iterator().next();
            String message = constraint.getMessage();
            System.out.println("Invalid constraint violation for field: " + message);
        }
    }

    /*********************************************************/

    public Address createAddress(String city, String country, String street) {
        Address addressEmbeddableEntity = new Address();
        addressEmbeddableEntity.setCity(city);
        addressEmbeddableEntity.setCountry(country);
        addressEmbeddableEntity.setStreet(street);
        return addressEmbeddableEntity;
    }

    public University createUniversity(String country, String name) {
        University university = new University();
        university.setCountry(country);
        university.setName(name);
        return university;
    }

    public Subject createSubject(String name) {
        Subject subject = new Subject();
        subject.setName(name);
        return subject;
    }

    public Student createStudent(Address address, String email, String firstName, String lastName, University university, String section, List<Subject> subjectList) {
        Student student = new Student();
        student.setAddress(address);
        student.setEmail(email);
        student.setFirst_name(firstName);
        student.setLast_name(lastName);
        student.setUniversity_id(university);
        student.setSection(section);
        student.setSubjectList(subjectList);
        return student;
    }

    public Grade createGrade(int grade, Subject subject, Student student) {
        Grade grade1 = new Grade();
        grade1.setGrade(grade);
        grade1.setSubject(subject);
        grade1.setStudent(student);
        return grade1;
    }

    /***********************************************************/

    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();

        Address address1 = createAddress("Timisoara", "Romania", "Ion CReanga");
        Address address2 = createAddress("Cluj", "Romania", "Titulescu");

        University university = createUniversity("Romania", "Politehnica");

        List<Subject> subject1 = new ArrayList<>();
        List<Subject> subject2 = new ArrayList<>();
        Subject info = createSubject("Info");
        Subject mate = createSubject("Mate");
        Subject romana = createSubject("Romana");
        subject1.add(info);
        subject1.add(mate);
        subject2.add(romana);

        List<Student> students = new ArrayList<>();
        Student student1 = createStudent(address1, "larisa.leucuta@gmail.com", "Larisa", "Leucuta", university, "Section1", subject1);
        Student student2 = createStudent(address1, "andrei@gmail.com", "Andrei", "Astanei", university, "Section1", subject1);
        Student student3 = createStudent(address2, "alex.gheorghe@gmail.com", "Alex", "Gheorghe", university, "Section2", subject2);
        students.add(student1);
        students.add(student2);
        students.add(student3);

        List<Grade> grades = new ArrayList<>();
        Grade grade1 = createGrade(10, info, student1);
        Grade grade2 = createGrade(9, mate, student1);
        Grade grade3 = createGrade(6, romana, student1);
        Grade grade4 = createGrade(9, info, student2);
        Grade grade5 = createGrade(10, romana, student3);
        Grade grade6 = createGrade(7, mate, student2);
        grades.add(grade1);
        grades.add(grade2);
        grades.add(grade3);
        grades.add(grade4);
        grades.add(grade5);
        grades.add(grade6);

        student1.setGrade(grades);
        student2.setGrade(grades);
        student3.setGrade(grades);

        em.persist(university);

        for (Subject sub : subject1) {
            em.persist(sub);
        }
        for (Subject sub : subject2) {
            em.persist(sub);
        }

        for (Student s : students) {
            em.persist(s);
        }
        utx.commit();
        em.clear();
    }

    @Override
    protected void internalClearData() {
        em.createQuery("delete from Student").executeUpdate();
    }


    @Override
    protected void startTransaction() {
        // Multiple transactions needed
    }

    @Override
    public void commitTransaction() {
        // Don't commit non existent transaction
    }

}


