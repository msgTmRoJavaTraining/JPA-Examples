package group.msg.examples.jpa.entity.day14;

import group.msg.examples.jpa.validator.CustomValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomValidatorStudentName implements ConstraintValidator<CustomValidationStudentName, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return !name.contains("@");
    }
}
