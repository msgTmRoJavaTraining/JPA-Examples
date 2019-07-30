package group.msg.examples.jpa.exerciseEntityPackage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CustomValidatorForCity  implements ConstraintValidator<CustomCityValidation, Student> {

    private boolean includeHardCodedValue;

    @Override
    public void initialize(CustomCityValidation validation) {
        includeHardCodedValue = validation.includeHardCodedValue();
    }

    @Override
    public boolean isValid(Student student, ConstraintValidatorContext validatorContext) {
        Address adr = student.getAddress();
        University uni = student.getUniversity();
        if(adr!= null && adr.getCountry().equals(uni.getCountry())){
            includeHardCodedValue= true;
        }else{
            includeHardCodedValue= false;
        }
        return includeHardCodedValue;
    }
}