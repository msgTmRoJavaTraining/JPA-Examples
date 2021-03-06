package group.msg.examples.jpa.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CustomValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface CustomValidation {
  String message() default "Custom validation did not pass!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean includeHardCodedValue() default true;
}
