package group.msg.test.jpa.day14;


import group.msg.examples.jpa.entity.day14.AdressEmbeddableEntity;
import group.msg.examples.jpa.entity.day14.StudentEntity;
import group.msg.examples.jpa.entity.day14.SubjectEntity;
import group.msg.examples.jpa.entity.day14.UniversityEntity;
import group.msg.examples.jpa.validator.ValidationEntity;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.ws.soap.Addressing;
import java.util.*;

@RunWith(Arquillian.class)
public class StudentTest extends JPABaseTest {

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
    public void createStudentEntity(){
        Query q = em.createNativeQuery("SELECT * FROM STUDENT_ENTITY");
        Assert.assertEquals("Entities not found in the database!",2, q.getResultList().size());
    }

    @Test
    public void findStudentEntity(){
        Query q = em.createNativeQuery("SELECT * FROM STUDENT_ENTITY WHERE FIRST_NAME ='Larisa'");
        Assert.assertEquals("Entities not found in the database!",1, q.getResultList().size());
    }

    @Test
    public void deleteStudentEntity(){
        StudentEntity studentEntity=em.find(StudentEntity.class,20);
        em.remove(studentEntity);
    }

    @Test
    public void validateName()throws Exception{
        utx.begin();
        em.joinTransaction();
        AdressEmbeddableEntity adressEmbeddableEntity= new AdressEmbeddableEntity();
        adressEmbeddableEntity.setCountry("Romania");
        adressEmbeddableEntity.setStreet("Lunga");
        adressEmbeddableEntity.setCity("Timisoara");

        UniversityEntity universityEntity=new UniversityEntity();
        universityEntity.setCountry("Romania");
        universityEntity.setName("Poli");

        em.persist(universityEntity);


        List<SubjectEntity> subjectEntities = new ArrayList<>();
        SubjectEntity subject = new SubjectEntity();
        subject.setName("Romana");
        subjectEntities.add(subject);

        for (SubjectEntity sub : subjectEntities){
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
            String message=constraint.getMessage();
            System.out.println("Invalid constraint violation for field: " + message);
        }
    }

    @Test
    public void validateAddress()throws Exception{
        utx.begin();
        em.joinTransaction();
        AdressEmbeddableEntity adressEmbeddableEntity= new AdressEmbeddableEntity();
        adressEmbeddableEntity.setCountry("Romania");
        adressEmbeddableEntity.setStreet("Lunga");
        adressEmbeddableEntity.setCity("Timisoara");

        UniversityEntity universityEntity=new UniversityEntity();
        universityEntity.setCountry("Romania");
        universityEntity.setName("Poli");

        em.persist(universityEntity);


        List<SubjectEntity> subjectEntities = new ArrayList<>();
        SubjectEntity subject = new SubjectEntity();
        subject.setName("Romana");
        subjectEntities.add(subject);

        for (SubjectEntity sub : subjectEntities){
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
            String message=constraint.getMessage();
            System.out.println("Invalid constraint violation for field: " + message);
        }
    }




    @Override
    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");

        AdressEmbeddableEntity adressEmbeddableEntity = new AdressEmbeddableEntity();
        adressEmbeddableEntity.setCity("Timisoara");
        adressEmbeddableEntity.setCountry("Romania");
        adressEmbeddableEntity.setStreet("Ion Creanga");

        UniversityEntity universityEntity = new UniversityEntity();
        universityEntity.setCountry("Romania");
        universityEntity.setName("Politehnica");


        em.persist(universityEntity);

        List<SubjectEntity> subjectEntities = new ArrayList<>();
        SubjectEntity subject = new SubjectEntity();
        subject.setName("Info");
        subjectEntities.add(subject);

        for (SubjectEntity sub : subjectEntities){
            em.persist(sub);
        }

        List<StudentEntity> studentEntities = new ArrayList<>();
        StudentEntity student=new StudentEntity();
        student.setAddress(adressEmbeddableEntity);
        student.setEmail("larisa.leucuta@yahoo.com");
        student.setFirst_name("Larisa");
        student.setLast_name("Leucuta");
        student.setUniversity_id(universityEntity);
        student.setSection("Mate");
        student.setSubjectList(subjectEntities);
        student.setId(20);
        studentEntities.add(student);

        StudentEntity student1=new StudentEntity();
        student1.setAddress(adressEmbeddableEntity);
        student1.setEmail("andrei@gmail.com");
        student1.setFirst_name("Andrei");
        student1.setLast_name("Astanei");
        student1.setUniversity_id(universityEntity);
        student1.setSection("Mate");
        student1.setSubjectList(subjectEntities);
        studentEntities.add(student1);

        for(StudentEntity s:studentEntities) {
            em.persist(s);
        }
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


