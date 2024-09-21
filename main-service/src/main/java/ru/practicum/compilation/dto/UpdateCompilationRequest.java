package ru.practicum.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class UpdateCompilationRequest {
    @Size(min = 1, max = 50, message = "Название подборки не может содержать больше 50 символов.")
    private String title;
    private Boolean pinned;
    private List<Long> events;
}
