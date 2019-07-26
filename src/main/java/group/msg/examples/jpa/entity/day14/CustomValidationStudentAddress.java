package group.msg.examples.jpa.entity.day14;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CustomValidatorStudentAddress.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface CustomValidationStudentAddress {
    String message() default "Stundetul si universitatea trebuie sa fie in acelasi oras!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
