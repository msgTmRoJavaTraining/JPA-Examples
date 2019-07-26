package group.msg.examples.jpa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CustomValidatorStudentName implements ConstraintValidator<MyCustomValidator, String> {

    @Override
    public void initialize(MyCustomValidator validation) {}

    private List<String> list= Arrays.asList("Marius","Ion");

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext)
    {
        return !list.contains(name);
    }
}