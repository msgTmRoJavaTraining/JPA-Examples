package group.msg.examples.jpa.validator;

import group.msg.examples.jpa.entity.StudentEntity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StudentLocationValidator implements ConstraintValidator<CheckStudentsLocation, StudentEntity> {
   public void initialize(CheckStudentsLocation constraint) {
   }

   @Override
   public boolean isValid(StudentEntity student, ConstraintValidatorContext constraintValidatorContext) {
      return student.getAddress().getCountry().equalsIgnoreCase(student.getUniversity().getCountry());
   }
}
