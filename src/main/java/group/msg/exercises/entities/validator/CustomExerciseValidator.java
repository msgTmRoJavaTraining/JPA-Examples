package group.msg.exercises.entities.validator;

import group.msg.examples.jpa.validator.CustomValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomExerciseValidator implements ConstraintValidator<CustomExerciseValidation, Integer> {

    private boolean includeHardCodedValue;

    @Override
    public void initialize(CustomExerciseValidation validation) {
        includeHardCodedValue = validation.banListForStudent();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext validatorContext) {
        if (value == null) {
            return true;
        }

        if (value == 123) {
            return includeHardCodedValue;
        }

        return value % 2 == 0;
    }
}
