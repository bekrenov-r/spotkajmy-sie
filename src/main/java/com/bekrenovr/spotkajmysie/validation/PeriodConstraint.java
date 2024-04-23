package com.bekrenovr.spotkajmysie.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PeriodValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
public @interface PeriodConstraint {
    String message() default "start time (%s) cannot come after end time (%s)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
