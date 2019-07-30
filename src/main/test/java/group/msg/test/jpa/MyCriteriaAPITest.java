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
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class MyCriteriaAPITest extends JPABaseTest {

    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery criteriaQuery;

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

        UniversityEntity univ3 = new UniversityEntity();
        univ3.setName("Faculty");
        univ3.setCountry("France");

        StudentEntity stud1 = new StudentEntity();
        stud1.setFirst_name("Popescu");
        stud1.setLast_name("Ion");

        StudentEntity stud2 = new StudentEntity();
        stud2.setFirst_name("Andreescu");
        stud2.setLast_name("Mihai");

        StudentEntity stud3 = new StudentEntity();
        stud3.setFirst_name("Francezu");
        stud3.setLast_name("dinFranta");

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

        EmbeddableAddressEntity address3 = new EmbeddableAddressEntity();
        address3.setCountry("France");
        address3.setCity("Paris");
        address3.setStreet("streetname");
        stud3.setAddress(address3);
        stud3.setUniversity(univ3);

        SubjectEntity subj1 = new SubjectEntity();
        subj1.setName("Mathematics");
        students.add(stud1);
        students.add(stud3);
        subj1.setStudents(students);

        GradeEntity grade1 = new GradeEntity();
        grade1.setGrade(9);
        grade1.setSubject(subj1);
        grade1.setStudent(stud1);

        GradeEntity grade2 = new GradeEntity();
        grade2.setGrade(6);
        grade2.setSubject(subj1);
        grade2.setStudent(stud1);

        GradeEntity grade3 = new GradeEntity();
        grade3.setGrade(5);
        grade3.setSubject(subj1);
        grade3.setStudent(stud2);

        GradeEntity grade4 = new GradeEntity();
        grade4.setGrade(9);
        grade4.setSubject(subj1);
        grade4.setStudent(stud2);

        GradeEntity grade5 = new GradeEntity();
        grade5.setGrade(4);
        grade5.setSubject(subj1);
        grade5.setStudent(stud3);

        em.persist(univ1);
        em.persist(univ2);
        em.persist(univ3);
        em.persist(subj1);
        em.persist(stud1);
        em.persist(stud2);
        em.persist(stud3);
        em.persist(grade1);
        em.persist(grade2);
        em.persist(grade3);
        em.persist(grade4);
        em.persist(grade5);

        utx.commit();
        em.clear();
    }

    @Test
    public void allStudentsFromCityWithCriteria() {
        String city = "Timisoara";

        criteriaBuilder = em.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery();

        Root<StudentEntity> root =
                criteriaQuery.from(em.getMetamodel().entity(StudentEntity.class));

        Join<StudentEntity, EmbeddableAddressEntity> joinedStudentAddress = root.join(StudentEntity_.address,
                JoinType.INNER);

        criteriaQuery.select(root).where(criteriaBuilder.equal(joinedStudentAddress.get(EmbeddableAddressEntity_.city), city));

        List<StudentEntity> list = em.createQuery(criteriaQuery).getResultList();
        logger.info("Students from " + city + ":");
        for (StudentEntity s : list
        ) {
            logger.info(s.getFirst_name() + " " + s.getLast_name());
        }
    }

    @Test
    public void allStudentsWithSubjectWithCriteria() {
        String subject = "Mathematics";

        criteriaBuilder = em.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery();

        Root<StudentEntity> root =
                criteriaQuery.from(em.getMetamodel().entity(StudentEntity.class));

        Join<StudentEntity, SubjectEntity> joinedSubjectTable = root.join(StudentEntity_.subjects,
                JoinType.LEFT);

        criteriaQuery.select(root).where(criteriaBuilder.equal(joinedSubjectTable.get(SubjectEntity_.name), subject));

        List<StudentEntity> list = em.createQuery(criteriaQuery).getResultList();
        Assert.assertEquals("Failed test!", 2, list.size());
        for (StudentEntity s : list
        ) {
            logger.info(subject + " \n" + s.getFirst_name() + " " + s.getLast_name());
        }
    }

    @Test
    public void averageGradeWithCriteria() {
        criteriaBuilder = em.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery();

        Root<StudentEntity> root =
                criteriaQuery.from(em.getMetamodel().entity(StudentEntity.class));

        Join<StudentEntity, GradeEntity> joinedGradesTable = root.join(StudentEntity_.grades,
                JoinType.LEFT);

        criteriaQuery.multiselect(criteriaBuilder.avg(joinedGradesTable.get(GradeEntity_.grade)),
                root.get(StudentEntity_.first_name),
                root.get(StudentEntity_.last_name))
                .groupBy(root.get(StudentEntity_.student_id), root.get(StudentEntity_.first_name), root.get(StudentEntity_.last_name));

        List averages = em.createQuery(criteriaQuery).getResultList();
        logger.info("Average Firstname Lastname");
        for (Object s : averages) {
            List<Object> list = Arrays.asList((Object[]) s);
            logger.info(list.get(0).toString() + " " + list.get(1).toString() + " " + list.get(2).toString());
        }
    }

    @Test
    public void averageGradeAbove8WithCriteria() {
        criteriaBuilder = em.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery();

        Root<StudentEntity> root =
                criteriaQuery.from(em.getMetamodel().entity(StudentEntity.class));

        Join<StudentEntity, GradeEntity> joinedGradesTable = root.join(StudentEntity_.grades,
                JoinType.LEFT);

        criteriaQuery.multiselect(criteriaBuilder.avg(joinedGradesTable.get(GradeEntity_.grade)),
                root.get(StudentEntity_.first_name),
                root.get(StudentEntity_.last_name))
                .groupBy(root.get(StudentEntity_.student_id), root.get(StudentEntity_.first_name), root.get(StudentEntity_.last_name))
                .having(criteriaBuilder.greaterThan(criteriaBuilder.avg(joinedGradesTable.get(GradeEntity_.grade)), 7.0));

        List averages = em.createQuery(criteriaQuery).getResultList();
        logger.info("Average Firstname Lastname");
        for (Object s : averages) {
            List<Object> list = Arrays.asList((Object[]) s);
            logger.info(list.get(0).toString() + " " + list.get(1).toString() + " " + list.get(2).toString());
        }
    }

    @Test
    public void studentsFromEveryCityWithCriteria() {
        criteriaBuilder = em.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery();

        Root<StudentEntity> root =
                criteriaQuery.from(em.getMetamodel().entity(StudentEntity.class));

        Join<StudentEntity, EmbeddableAddressEntity> joinedStudentAddress = root.join(StudentEntity_.address,
                JoinType.INNER);

        criteriaQuery.multiselect(criteriaBuilder.count(root.get(StudentEntity_.student_id)), joinedStudentAddress.get(EmbeddableAddressEntity_.city))
                .groupBy(joinedStudentAddress.get(EmbeddableAddressEntity_.city));

        List<StudentEntity> result = em.createQuery(criteriaQuery).getResultList();
        logger.info("Total City");
        for (Object s : result) {
            List<Object> resultAsArray = Arrays.asList((Object[]) s);
            logger.info(resultAsArray.get(0).toString() + " " + resultAsArray.get(1).toString());
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
