package group.msg.examples.jpa.entity.day14;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


    @Constraint(validatedBy= CustomValidationForAdress.class)
    @Target({TYPE})
    @Retention(RUNTIME)
    public @interface CustomForAddress {
        String message() default "Country does not match!!";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        boolean includeHardCodedValue() default true;
}
