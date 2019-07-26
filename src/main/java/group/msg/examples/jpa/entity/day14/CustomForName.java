package group.msg.examples.jpa.entity.day14;


import group.msg.examples.jpa.validator.CustomValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CustomValidationForName.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface CustomForName {
    String message() default "You are banned from school!!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean includeHardCodedValue() default true;
}
