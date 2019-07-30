package group.msg.examples.jpa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomValidator implements ConstraintValidator<CustomValidation, Integer> {

    private boolean includeHardCodedValue;

    @Override
    public void initialize(CustomValidation validation) {
        includeHardCodedValue = validation.includeHardCodedValue();
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
