package com.proyecto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "La fecha de fin no puede ser anterior a la fecha de inicio";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String startField();
    String endField();
}