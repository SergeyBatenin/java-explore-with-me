package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@Valid @RequestBody NewEventDto eventDto,
                               @PathVariable @Positive long userId) {
        log.info("POST /users/{}/events request: {}", userId, eventDto);
        EventFullDto eventFullDto = eventService.create(userId, eventDto);
        log.info("POST /users/{}/events response: {}", userId, eventFullDto);
        return eventFullDto;
    }

    @GetMapping
    public List<EventShortDto> getAll(@PathVariable @Positive long userId,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                      @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET ALL /users/{}/events request: from={}, size={}", userId, from, size);
        List<EventShortDto> events = eventService.getAll(userId, from, size);
        log.info("GET ALL /users/{}/events response: {}", userId, events.size());
        return events;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable @Positive long userId, @PathVariable @Positive long eventId) {
        log.info("GET BY ID /users/{}/events/{} request", userId, eventId);
        EventFullDto event = eventService.getById(userId, eventId);
        log.info("GET BY ID /users/{}/events/{} response: {}", userId, eventId, event);
        return event;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByCreator(@PathVariable @Positive long userId,
                                        @PathVariable @Positive long eventId,
                                        @RequestBody @Valid UpdateEventUserRequest updateRequest) {
        log.info("USER PATCH BY ID /users/{}/events/{} request", userId, eventId);
        EventFullDto event = eventService.updateByCreator(userId, eventId, updateRequest);
        log.info("USER PATCH BY ID /users/{}/events/{} response: {}", userId, eventId, event);
        return event;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsToCreator(@PathVariable @Positive long userId,
                                                              @PathVariable @Positive long eventId) {
        log.info("GET REQUESTS TO CREATOR /users/{}/events/{}/requests request", userId, eventId);
        List<ParticipationRequestDto> requests = eventService.getRequests(userId, eventId);
        log.info("GET REQUESTS TO CREATOR /users/{}/events/{}/requests response: {}", userId, eventId, requests.size());
        return requests;
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult replyToParticipationRequests(@PathVariable @Positive long userId,
                                                                       @PathVariable @Positive long eventId,
                                                                       @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        log.info("PATCH REPLY TO REQUESTS /users/{}/events/{}/requests request: {}", userId, eventId, updateRequest);
        EventRequestStatusUpdateResult result = eventService.replyToRequests(userId, eventId, updateRequest);
        log.info("PATCH REPLY TO REQUESTS /users/{}/events/{}/requests response: {}", userId, eventId, result);
        return result;
    }
}
