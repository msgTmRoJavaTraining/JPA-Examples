package group.msg.examples.jpa.entity.day14;

import group.msg.examples.jpa.validator.CustomValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CustomValidationForName implements ConstraintValidator<CustomForName,String> {


    private boolean includeHardCodedValue;

    @Override
    public void initialize(CustomForName customForName) {
        includeHardCodedValue = customForName.includeHardCodedValue();
    }

    @Override
    public boolean isValid(String firstName, ConstraintValidatorContext constraintValidatorContext) {
        List<String> banStudentList = new ArrayList<>();
        banStudentList.add("Ion");
        banStudentList.add("Mihai");

        return !banStudentList.contains(firstName);
    }
}
