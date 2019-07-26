package group.msg.examples.jpa.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DifferentCityValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface CityValidator
{
    String message() default "The city doesn t match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean includeHardCodedValue() default true;

}
