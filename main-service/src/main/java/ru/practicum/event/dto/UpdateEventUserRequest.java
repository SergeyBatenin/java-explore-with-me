package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.event.model.UserStateAction;

@Data
public class UpdateEventUserRequest extends UpdateEventRequest {
    private UserStateAction stateAction;
}
