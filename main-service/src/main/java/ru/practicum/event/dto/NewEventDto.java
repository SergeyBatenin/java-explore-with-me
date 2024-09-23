package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.validation.EventDateValidation;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 3, max = 120, message = "Название события должно содержать от 3 до 120 символов")
    private String title;
    @NotBlank
    @Size(min = 20, max = 2000, message = "Аннотация события должна содержать от 20 до 2000 символов")
    private String annotation;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Описание события должно содержать от 20 до 7000 символов")
    private String description;
    @NotNull
    @Positive
    private Long category;
    @NotNull
    @EventDateValidation
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    @Valid
    private LocationDto location;
    private boolean paid = false;
    @PositiveOrZero
    private int participantLimit = 0;
    private boolean requestModeration = true;
}
