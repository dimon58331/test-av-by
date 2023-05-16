package by.av.test.testavby.annotation;

import by.av.test.testavby.validator.ReleaseYearsMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseYearsMatchesValidator.class)
public @interface ReleaseYearsMatches {
    String message() default "End release year cannot be less than start release year";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
