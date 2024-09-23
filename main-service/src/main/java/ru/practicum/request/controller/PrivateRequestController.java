package ru.practicum.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable @Positive long userId,
                                          @RequestParam @Positive long eventId) {
        log.info("POST /users/{}/requests?eventId={} request", userId, eventId);
        ParticipationRequestDto requestDto = requestService.create(userId, eventId);
        log.info("POST /users/{}/requests?eventId={} response: {}", userId, eventId, requestDto);
        return requestDto;
    }

    @GetMapping
    public List<ParticipationRequestDto> getAll(@PathVariable @Positive long userId) {
        log.info("GET /users/{}/requests request", userId);
        List<ParticipationRequestDto> requestDtos = requestService.getAll(userId);
        log.info("GET /users/{}/requests response: {}", userId, requestDtos.size());
        return requestDtos;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive long userId,
                                          @PathVariable @Positive long requestId) {
        log.info("PATCH /users/{}/requests/{}/cancel request", userId, requestId);
        ParticipationRequestDto requestDto = requestService.cancel(userId, requestId);
        log.info("PATCH /users/{}/requests/{}/cancel response: {}", userId, requestId, requestDto);
        return requestDto;
    }
}
