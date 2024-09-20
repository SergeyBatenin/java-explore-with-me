package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamHitDto {
    @NotBlank
    @Size(max = 30, message = "Превышен максимальный размер названия приложения")
    private String app;
    @NotBlank
    @Size(max = 500, message = "Превышен максимальный размер url'а")
    private String uri;
    @NotNull
    @Size(max = 15, min = 7)
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|$)){4}$",
            message = "Некорректный формат IP-адреса")
    private String ip;
    @NotNull
    private LocalDateTime timestamp;
}
