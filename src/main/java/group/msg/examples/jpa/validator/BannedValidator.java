package group.msg.examples.jpa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class BannedValidator implements ConstraintValidator<BannedNames, String> {
    private String[] banned={"Alex","Vlad"};

    @Override
    public void initialize(BannedNames constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        for(int i=0;i<banned.length;i++)
        {
            if(s.equals(banned[i]))
                return false;
        }
        return true;
    }
}
