package ru.practicum.event.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class UpdateEventDateValidator implements ConstraintValidator<UpdateEventDateValidation, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDate == null) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now().plusHours(2);
        return now.isBefore(eventDate);
    }
}
