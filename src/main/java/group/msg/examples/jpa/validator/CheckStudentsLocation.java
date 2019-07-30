package group.msg.examples.jpa.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = StudentLocationValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface CheckStudentsLocation {
    String message() default "Sorry, but this student is from another country!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
