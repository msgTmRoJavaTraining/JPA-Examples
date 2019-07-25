package group.msg.examples.jpa.exercise_entityPackage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CustomValidatorForCity  implements ConstraintValidator<CustomNameValidation, Student> {

    private boolean includeHardCodedValue;

    @Override
    public void initialize(CustomNameValidation validation) {
        includeHardCodedValue = validation.includeHardCodedValue();
    }

    @Override
    public boolean isValid(Student student, ConstraintValidatorContext validatorContext) {
        Address adr = student.getAddress();
        University uni = student.getUniversity();
        if(adr.getCountry().equals(uni.getCountry())){
            includeHardCodedValue= true;
        }else{
            includeHardCodedValue= false;
        }
        return includeHardCodedValue;
    }
}