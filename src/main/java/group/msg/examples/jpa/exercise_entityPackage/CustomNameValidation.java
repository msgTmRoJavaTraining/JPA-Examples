package group.msg.examples.jpa.exercise_entityPackage;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CustomValidatorForStudentNames.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface CustomNameValidation {
    String message() default "Custom validation for first name did not pass!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean includeHardCodedValue() default true;
}
