package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.event.model.AdminStateAction;

@Data
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private AdminStateAction stateAction;
}
