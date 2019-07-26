package group.msg.examples.jpa.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = BannedValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface BannedNames {
    String message() default "Banned name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
