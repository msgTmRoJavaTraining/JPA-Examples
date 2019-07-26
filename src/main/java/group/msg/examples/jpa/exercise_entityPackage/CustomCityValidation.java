package group.msg.examples.jpa.exercise_entityPackage;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CustomValidatorForCity.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface CustomCityValidation {
    String message() default "Custom validation for country did not pass!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean includeHardCodedValue() default true;
}
