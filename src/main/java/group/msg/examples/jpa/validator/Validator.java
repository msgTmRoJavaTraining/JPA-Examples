package group.msg.examples.jpa.validator;

import group.msg.exercises.entities.Student;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Validator implements ConstraintValidator<StudentLocation, Student> {

    public void initialize(StudentLocation constraint) {
    }

    @Override
    public boolean isValid(Student student, ConstraintValidatorContext constraintValidatorContext) {
        boolean tmp = false;

        tmp = student.getAdress().getCountry().equals(student.getUniversity_id().getCountry());

        if (tmp)
            return true;

        return false;
    }


}
