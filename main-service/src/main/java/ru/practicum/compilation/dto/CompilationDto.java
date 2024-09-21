package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompilationDto {
    private long id;
    private String title;
    private boolean pinned;
    @ToString.Exclude
    private List<EventShortDto> events;
}
