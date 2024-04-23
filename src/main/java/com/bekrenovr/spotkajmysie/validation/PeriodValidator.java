package com.bekrenovr.spotkajmysie.validation;

import com.bekrenovr.spotkajmysie.config.LocalTimeSerializer;
import com.bekrenovr.spotkajmysie.dto.Period;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeriodValidator implements ConstraintValidator<PeriodConstraint, Period> {
    @Override
    public boolean isValid(Period period, ConstraintValidatorContext context) {
        if (period.start().isAfter(period.end())) {
            addConstraintViolationToContext(period, context);
            return false;
        }
        return true;
    }

    private void addConstraintViolationToContext(Period period, ConstraintValidatorContext context){
        String message = String.format(
                context.getDefaultConstraintMessageTemplate(),
                period.start().format(LocalTimeSerializer.LOCAL_TIME_FORMATTER),
                period.end().format(LocalTimeSerializer.LOCAL_TIME_FORMATTER)
        );
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
