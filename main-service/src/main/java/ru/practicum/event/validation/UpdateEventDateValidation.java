package ru.practicum.event.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UpdateEventDateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface UpdateEventDateValidation {
    String message() default "Начало мероприятия должно быть позже текущего времени, но не менее, чем на 2 часа!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
