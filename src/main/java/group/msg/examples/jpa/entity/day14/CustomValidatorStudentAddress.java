package group.msg.examples.jpa.entity.day14;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomValidatorStudentAddress implements ConstraintValidator<CustomValidationStudentAddress, StudentEntity> {

    @Override
    public boolean isValid(StudentEntity studentEntity, ConstraintValidatorContext constraintValidatorContext) {
        String studentCountry = studentEntity.getHome_address().getCountry();
        String universityCountry = studentEntity.getUniversity_id().getCountry();

        return studentCountry.equals(universityCountry);
    }
}
