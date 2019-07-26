package group.msg.examples.jpa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyCustomValidator implements ConstraintValidator<IsThisStudentBannedOrNot, String> {
   private String[] bannedNames = {"Smith","Johnson","Stifler","Andrew"};

   public void initialize(IsThisStudentBannedOrNot constraint) {
   }

   public boolean isValid(String obj, ConstraintValidatorContext context) {

      for (String str:bannedNames
           ) {
         if(obj.contains(str))
            return false;
      }
      return true;
   }
}
