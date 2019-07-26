package group.msg.examples.jpa.entity.day14;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CustomValidationForAdress implements ConstraintValidator<CustomForAddress,StudentEntity> {

    private boolean includeHardCodedValue;

    @Override
    public void initialize(CustomForAddress customForAddress) {
        includeHardCodedValue = customForAddress.includeHardCodedValue();
    }

    @Override
    public boolean isValid(StudentEntity studentEntity, ConstraintValidatorContext constraintValidatorContext) {
        AdressEmbeddableEntity adr=studentEntity.getAddress();
        UniversityEntity uni=studentEntity.getUniversity_id();
        return adr.getCountry().equals(uni.getCountry());
    }


}
