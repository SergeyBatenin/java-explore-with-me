package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.dto.AdminParamSearchEvent;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.PublicParamSearchEvent;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    //region PrivateApi
    EventFullDto create(long userId, NewEventDto eventDto);

    List<EventShortDto> getAll(long userId, int from, int size);

    EventFullDto getById(long userId, long eventId);

    EventFullDto updateByCreator(long userId, long eventId, UpdateEventUserRequest updateRequest);

    List<ParticipationRequestDto> getRequests(long userId, long eventId);

    EventRequestStatusUpdateResult replyToRequests(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest);
    //endregion PrivateApi

    //region AdminApi
    EventFullDto updateByAdmin(long eventId, UpdateEventAdminRequest updateRequest);

    List<EventFullDto> search(AdminParamSearchEvent paramSearchEvent);
    //endregion AdminApi

    //region PublicApi
    List<EventShortDto> showEvents(PublicParamSearchEvent paramSearchEvent, HttpServletRequest request);

    EventFullDto showById(long eventId, HttpServletRequest request);
    //endregion PublicApi
}
