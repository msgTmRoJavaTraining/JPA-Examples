package group.msg.test.jpa.validator;


import group.msg.examples.jpa.entity.HomeAddress;
import group.msg.examples.jpa.entity.StudentEntity;
import group.msg.examples.jpa.entity.Subject;
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
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.util.ArrayList;

@RunWith(Arquillian.class)
public class StudentNameValidTest extends JPABaseTest
{

    @PersistenceUnit(unitName = "testStud")
    private EntityManagerFactory emf;



    @Test
    public void testValidationEntity() throws Exception{



    }


    private void validate(String name) throws SystemException, NotSupportedException {

        EntityManager entityManager = emf.createEntityManager();
        utx.begin();
        entityManager.joinTransaction();

        University university=new University();
        university.setName("UPT");
        university.setCountry("Romania");


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
