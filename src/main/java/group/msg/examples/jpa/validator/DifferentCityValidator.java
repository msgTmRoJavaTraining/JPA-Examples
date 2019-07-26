package group.msg.examples.jpa.validator;

import group.msg.examples.jpa.entity.StudentEntity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DifferentCityValidator implements ConstraintValidator<CityValidator, StudentEntity>
{
    @Override
    public void initialize(CityValidator validation) {}

    @Override
    public boolean isValid(StudentEntity studentEntity, ConstraintValidatorContext constraintValidatorContext)
    {
        return (studentEntity.getHomeAddress().getCountry()).equals(studentEntity.getUniversity().getCountry());
    }
}