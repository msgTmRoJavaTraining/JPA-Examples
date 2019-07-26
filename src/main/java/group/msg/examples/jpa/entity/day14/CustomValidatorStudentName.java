package group.msg.examples.jpa.entity.day14;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomValidatorStudentName implements ConstraintValidator<CustomValidationStudentName, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        char[] forbiddenCharacters = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '-', '=', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        for (char forbiddenCharacter : forbiddenCharacters) {
            if (name.indexOf(forbiddenCharacter) != -1) {
                return false;
            }
        }

        return true;
    }
}
