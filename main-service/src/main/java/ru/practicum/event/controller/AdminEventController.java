package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.AdminParamSearchEvent;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.State;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.DateTimeException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) List<State> states,
                                     @RequestParam(required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                     @RequestParam(required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "10") @Positive int size) {
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new DateTimeException("Дата начала периода поиска не может быть позже даты окончания периода.");
        }
        AdminParamSearchEvent paramSearchEvent = AdminParamSearchEvent.builder()
                .users(users)
                .categories(categories)
                .states(states)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        log.info("GET ALL /admin/events request: {}", paramSearchEvent);
        List<EventFullDto> events = eventService.search(paramSearchEvent);
        log.info("GET ALL /admin/events response: {}", events.size());
        return events;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByAdmin(@PathVariable @Positive long eventId,
                                      @RequestBody @Valid UpdateEventAdminRequest updateRequest) {
        log.info("ADMIN PATCH BY ID /admin/events/{} request: {}", eventId, updateRequest);
        EventFullDto event = eventService.updateByAdmin(eventId, updateRequest);
        log.info("ADMIN PATCH BY ID /admin/events/{} response: {}", eventId, event);
        return event;
    }
}
