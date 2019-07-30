package group.msg.examples.jpa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CustomValidator implements ConstraintValidator<CustomValidation, String> {

  private boolean valueBaned;
  private List<String> name = new ArrayList<>();

  @Override
  public void initialize(CustomValidation validation) {
    valueBaned = validation.includeHardCodedValue();
  }

    public static String[] names = new String[10];

    private void setName(String[] str) {
        names = str;

    }
  @Override
  public boolean isValid(String str, ConstraintValidatorContext validatorContext) {

      for (String string : names
      ) {
          if (string.contains(str))
              return false;
      }
      return true;
  }
}
