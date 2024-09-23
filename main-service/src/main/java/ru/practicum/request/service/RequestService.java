package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(long userId, long eventId);

    List<ParticipationRequestDto> getAll(long userId);

    ParticipationRequestDto cancel(long userId, long requestId);
}
