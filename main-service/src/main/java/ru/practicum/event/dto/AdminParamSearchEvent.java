package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class AdminParamSearchEvent {
    private Set<Long> users;
    private Set<Long> categories;
    private Set<State> states;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;
    private int from;
    private int size;
}
