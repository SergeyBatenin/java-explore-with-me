package ru.practicum.event.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Location {
    @Min(value = -90, message = "Значение широты не может быть меньше -90")
    @Max(value = 90, message = "Значение широты не может быть больше 90")
    private Double lat;
    @Min(value = -180, message = "Значение долготы не может быть меньше -180")
    @Max(value = 180, message = "Значение долготы не может быть больше 180")
    private Double lon;
}
