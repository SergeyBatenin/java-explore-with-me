package ru.practicum.event.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @NotNull
    @Min(value = -90, message = "Значение широты не может быть меньше -90")
    @Max(value = 90, message = "Значение широты не может быть больше 90")
    private Double lat;
    @NotNull
    @Min(value = -180, message = "Значение долготы не может быть меньше -180")
    @Max(value = 180, message = "Значение долготы не может быть больше 180")
    private Double lon;
}
