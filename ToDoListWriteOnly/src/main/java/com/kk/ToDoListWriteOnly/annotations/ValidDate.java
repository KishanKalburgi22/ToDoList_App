package com.kk.ToDoListWriteOnly.annotations;

import com.kk.ToDoListWriteOnly.validators.DatesValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DatesValidator.class)
@Documented
public @interface ValidDate {
    String message() default "{ValidDate.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
