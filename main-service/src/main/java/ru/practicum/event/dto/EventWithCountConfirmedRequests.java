package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class EventWithCountConfirmedRequests {
    private long eventId;
    private long confirmedRequests;
}
