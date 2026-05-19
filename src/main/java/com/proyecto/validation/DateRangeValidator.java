package com.proyecto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(ValidDateRange annotation) {
        this.startField = annotation.startField();
        this.endField   = annotation.endField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext ctx) {
        try {
            Field start = obj.getClass().getDeclaredField(startField);
            Field end   = obj.getClass().getDeclaredField(endField);
            start.setAccessible(true);
            end.setAccessible(true);

            Object startVal = start.get(obj);
            Object endVal   = end.get(obj);

            // Si alguno es null, otras anotaciones lo capturan
            if (startVal == null || endVal == null) return true;

            if (startVal instanceof LocalDate s && endVal instanceof LocalDate e) {
                return !e.isBefore(s);
            }
            if (startVal instanceof LocalDateTime s && endVal instanceof LocalDateTime e) {
                return !e.isBefore(s);
            }

            return true;

        } catch (Exception ex) {
            return true;
        }
    }
}