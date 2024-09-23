package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank(message = "Название подборки не может быть пустым")
    @Size(min = 1, max = 50, message = "Название подборки не может содержать больше 50 символов.")
    private String title;
    private boolean pinned = false;
    private Set<Long> events = new HashSet<>();
}
