package group.msg.test.jpa.entity;


import group.msg.examples.jpa.entity.StudentEntity;
import group.msg.examples.jpa.entity.University;
import group.msg.examples.jpa.validator.CustomValidation;
import group.msg.examples.jpa.validator.CustomValidator;
import group.msg.examples.jpa.validator.ValidationEntity;
import group.msg.test.jpa.JPABaseTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

@RunWith(Arquillian.class)
public class ValidNameTest extends JPABaseTest
{

    @PersistenceUnit(unitName = "test")
    private EntityManagerFactory emf;


    @Test
    public void testValidationEntity() throws Exception{

        validateObject();

    }


    private void validateObject() throws Exception {

        EntityManager entityManager = emf.createEntityManager();
        utx.begin();
        entityManager.joinTransaction();

//
        StudentEntity studentEntity=new StudentEntity();


        //StudentEntity studentEntity=new StudentEntity();




        // university.setStudentEntityList();
        //
//        ArrayList<Subject>subjects=new ArrayList<>();
//
//        Subject sub1=new Subject();
//        Subject s2=new Subject();
//        s2.setName("fizica");
//        sub1.setName("chimie");
//
//        subjects.add(sub1);
//        subjects.add(s2);

//
//        StudentEntity student=new StudentEntity();
//
//        student.setFirst_name("Ion");
//        student.setLast_name("Baciu");
//        student.setHomeAddress(new HomeAddress("Parvan","Timisoara","Romania"));
//        student.setUniversity(university);
//        student.setSection("CTI");
//        student.setSubjects(subjects);
//

        try {
            entityManager.persist(studentEntity);
//
//            for(Subject s:subjects)
//                entityManager.persist(s);
//
            //entityManager.persist(studentEntity);
            utx.commit();
        } catch (ConstraintViolationException e) {
            utx.rollback();
        }
    }



    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addClass(ValidationEntity.class).addClass(CustomValidation.class).addClass(CustomValidator.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
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
