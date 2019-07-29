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
        TypedQuery<StudentEntity> location = em.createQuery("select se.first_name from StudentEntity se where se.address.city like 'Timisoara'", StudentEntity.class);
        Assert.assertEquals("Entities not found in the database!", 2, location.getResultList().size());
        System.out.println("Students that live in this city are: " + location.getResultList().toString());
    }

    @Test
    public void studentsCertainSubject() {
        TypedQuery<StudentEntity> subject = em.createQuery("select distinct stud.first_name from StudentEntity stud join fetch stud.subjectList sub where sub.name='Mate'", StudentEntity.class);
        System.out.println("Students with this subject are: " + subject.getResultList().toString());
    }

    @Test
    public void averageGrade() {
        TypedQuery<StudentEntity> average = em.createQuery("select stud.first_name, avg(grade.grade) from StudentEntity stud join fetch stud.grade grade group by stud.first_name", StudentEntity.class);

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
        TypedQuery<StudentEntity> average = em.createQuery("select stud.first_name, avg(grade.grade) from StudentEntity stud join fetch stud.grade grade group by stud.first_name having avg(grade.grade) > 8", StudentEntity.class);

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
        TypedQuery<StudentEntity> count = em.createQuery("select  stud.address.city, count(stud.first_name) from StudentEntity stud group by stud.address.city", StudentEntity.class);

        List response = count.getResultList();
        response.forEach(r -> {
            if (r instanceof Object[]) {
                List s = Arrays.asList((Object[]) r);
                logger.info(s.get(0).toString() + " has " + s.get(1).toString() + " students ");
            }
        });
    }


    @Test
    public void createStudentEntity() {
        Query q = em.createNativeQuery("SELECT * FROM STUDENT_ENTITY");
        Assert.assertEquals("Entities not found in the database!", 3, q.getResultList().size());
    }

    @Test
    public void findStudentEntity() {
        Query q = em.createNativeQuery("SELECT * FROM STUDENT_ENTITY WHERE FIRST_NAME ='Larisa'");
        Assert.assertEquals("Entities not found in the database!", 1, q.getResultList().size());
    }

    @Test
    public void deleteStudentEntity() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        utx.begin();
        em.joinTransaction();
        StudentEntity studentEntity = em.find(StudentEntity.class, 50);
        em.remove(studentEntity);
        utx.commit();

        em.clear();
    }

    @Test
    public void validateName() throws Exception {
        utx.begin();
        em.joinTransaction();
        AdressEmbeddableEntity adressEmbeddableEntity = new AdressEmbeddableEntity();
        adressEmbeddableEntity.setCountry("Romania");
        adressEmbeddableEntity.setStreet("Lunga");
        adressEmbeddableEntity.setCity("Timisoara");

        UniversityEntity universityEntity = new UniversityEntity();
        universityEntity.setCountry("Romania");
        universityEntity.setName("Poli");

        em.persist(universityEntity);

        List<SubjectEntity> subjectEntities = new ArrayList<>();
        SubjectEntity subject = new SubjectEntity();
        subject.setName("Romana");
        subjectEntities.add(subject);

        for (SubjectEntity sub : subjectEntities) {
            em.persist(sub);
        }

        StudentEntity s = new StudentEntity();
        s.setAddress(adressEmbeddableEntity);
        s.setEmail("alex@gmail.com");
        s.setFirst_name("jf");
        s.setLast_name("Gheorghe");
        s.setUniversity_id(universityEntity);
        s.setSection("Mec");
        s.setSubjectList(subjectEntities);

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
        AdressEmbeddableEntity adressEmbeddableEntity = new AdressEmbeddableEntity();
        adressEmbeddableEntity.setCountry("Romania");
        adressEmbeddableEntity.setStreet("Lunga");
        adressEmbeddableEntity.setCity("Timisoara");

        UniversityEntity universityEntity = new UniversityEntity();
        universityEntity.setCountry("Romania");
        universityEntity.setName("Poli");

        em.persist(universityEntity);


        List<SubjectEntity> subjectEntities = new ArrayList<>();
        SubjectEntity subject = new SubjectEntity();
        subject.setName("Romana");
        subjectEntities.add(subject);

        for (SubjectEntity sub : subjectEntities) {
            em.persist(sub);
        }

        StudentEntity s = new StudentEntity();
        s.setAddress(adressEmbeddableEntity);
        s.setEmail("alex@gmail.com");
        s.setFirst_name(null);
        s.setLast_name("Gheorghe");
        s.setUniversity_id(universityEntity);
        s.setSection("Mec");
        s.setSubjectList(subjectEntities);


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


    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");

        AdressEmbeddableEntity addressEmbeddableEntity = new AdressEmbeddableEntity();
        addressEmbeddableEntity.setCity("Timisoara");
        addressEmbeddableEntity.setCountry("Romania");
        addressEmbeddableEntity.setStreet("Ion Creanga");

        AdressEmbeddableEntity addressEmbeddableEntity1 = new AdressEmbeddableEntity();
        addressEmbeddableEntity1.setCity("Cluj");
        addressEmbeddableEntity1.setCountry("Romania");
        addressEmbeddableEntity1.setStreet("Titulescu");

        UniversityEntity universityEntity = new UniversityEntity();
        universityEntity.setCountry("Romania");
        universityEntity.setName("Politehnica");


        List<SubjectEntity> subjectEntities = new ArrayList<>();

        SubjectEntity info = new SubjectEntity();
        info.setName("Info");

        SubjectEntity mate = new SubjectEntity();
        mate.setName("Mate");


        List<SubjectEntity> subjectEntities1 = new ArrayList<>();
        SubjectEntity romana = new SubjectEntity();
        romana.setName("Romana");

        subjectEntities.add(info);
        subjectEntities.add(mate);
        subjectEntities1.add(romana);


        List<StudentEntity> studentEntities = new ArrayList<>();

        StudentEntity student = new StudentEntity();
        student.setAddress(addressEmbeddableEntity);
        student.setEmail("larisa.leucuta@yahoo.com");
        student.setFirst_name("Larisa");
        student.setLast_name("Leucuta");
        student.setUniversity_id(universityEntity);
        student.setSection("Section1");
        student.setSubjectList(subjectEntities);
        student.setId(50);
        studentEntities.add(student);

        StudentEntity student1 = new StudentEntity();
        student1.setAddress(addressEmbeddableEntity1);
        student1.setEmail("andrei@gmail.com");
        student1.setFirst_name("Andrei");
        student1.setLast_name("Astanei");
        student1.setUniversity_id(universityEntity);
        student1.setSection("Section1");
        student1.setSubjectList(subjectEntities);
        studentEntities.add(student1);

        StudentEntity student2 = new StudentEntity();
        student2.setAddress(addressEmbeddableEntity);
        student2.setEmail("alex@gmail.com");
        student2.setFirst_name("Alex");
        student2.setLast_name("Gheorghe");
        student2.setUniversity_id(universityEntity);
        student2.setSection("Section2");
        student2.setSubjectList(subjectEntities1);
        studentEntities.add(student2);

        List<GradeEntity> grades = new ArrayList<>();

        GradeEntity grade = new GradeEntity();
        grade.setGrade(10);
        grade.setSubject(info);
        grade.setStudentEntity(student);

        GradeEntity grade1 = new GradeEntity();
        grade1.setGrade(9);
        grade1.setSubject(mate);
        grade1.setStudentEntity(student1);

        GradeEntity grade2 = new GradeEntity();
        grade2.setGrade(8);
        grade2.setSubject(romana);
        grade2.setStudentEntity(student2);

        GradeEntity grade3 = new GradeEntity();
        grade3.setGrade(5);
        grade3.setSubject(mate);
        grade3.setStudentEntity(student);

        grades.add(grade);
        grades.add(grade1);
        grades.add(grade2);
        grades.add(grade3);

        student.setGrade(grades);
        student1.setGrade(grades);
        student2.setGrade(grades);

        em.persist(universityEntity);

        for (SubjectEntity sub : subjectEntities) {
            em.persist(sub);
        }
        for (SubjectEntity sub : subjectEntities1) {
            em.persist(sub);
        }

        for (StudentEntity s : studentEntities) {
            em.persist(s);
        }

//        for (GradeEntity g : grades) {
//            em.persist(g);
//        }

        utx.commit();

        em.clear();
    }

    @Override
    protected void internalClearData() {
        em.createQuery("delete from StudentEntity").executeUpdate();
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


