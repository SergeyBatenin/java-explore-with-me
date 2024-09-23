package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.validation.UpdateEventDateValidation;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    @Size(min = 3, max = 120, message = "Название события должно содержать от 3 до 120 символов")
    protected String title;
    @Size(min = 20, max = 2000, message = "Аннотация события должна содержать от 20 до 2000 символов")
    protected String annotation;
    @Size(min = 20, max = 7000, message = "Описание события должно содержать от 20 до 7000 символов")
    protected String description;
    @Positive(message = "Некорректная категория события")
    protected Long category;
    @UpdateEventDateValidation
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate;
    protected LocationDto location;
    protected Boolean paid;
    @PositiveOrZero(message = "Некорректное количество участников события")
    protected Integer participantLimit;
    protected Boolean requestModeration;
}
