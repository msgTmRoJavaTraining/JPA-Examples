package group.msg.examples.jpa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CustomValidator implements ConstraintValidator<CustomValidation, Integer> {

  private boolean valueBaned;
  private List<String> name = new ArrayList<>();

  @Override
  public void initialize(CustomValidation validation) {
    valueBaned = validation.includeHardCodedValue();
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext validatorContext) {
    if (value == null) {
      return true;
    }

    if (value == 123) {
        return valueBaned;
    }

    return value % 2 == 0;
  }
}
