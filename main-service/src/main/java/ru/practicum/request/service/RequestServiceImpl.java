package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.EventAvailableException;
import ru.practicum.exception.RequestLimitException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.mapper.RequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Transactional
    @Override
    public ParticipationRequestDto create(long userId, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.debug("GET EVENT. Событие с айди {} не найден.", eventId);
                    return new DataNotFoundException("Событие с id=" + eventId + " не существует.");
                });
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.debug("GET USER. Пользователь с айди {} не найден.", userId);
                    return new DataNotFoundException("Пользователь с id=" + userId + " не существует.");
                });
        checkEventAvailable(event, userId);

        List<ParticipationRequest> requestOpt = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (!requestOpt.isEmpty()) {
            log.debug("CHECK DUPLICATE REQUEST. Заявка с айди заявителя {} на событие с айди {} уже существует.", userId, eventId);
            throw new EventAvailableException("Нельзя повторно отправить заявку");
        }


        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setRequester(user);
        request.setEvent(event);

        request.setStatus(
                !event.isRequestModeration() || event.getParticipantLimit() == 0
                        ? RequestStatus.CONFIRMED
                        : RequestStatus.PENDING);
        requestRepository.save(request);
        return requestMapper.toDto(request);
    }

    private void checkEventAvailable(Event event, long userId) {
        //+ инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
        if (event.getInitiator().getId() == userId) {
            throw new EventAvailableException("Нельзя подать заявку на собственное событие");
        }
        //+ нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
        if (event.getState() != State.PUBLISHED) {
            throw new EventAvailableException("Подать заявку можно только на опубликованные события");
        }

        long participantLimit = event.getParticipantLimit();
        if (participantLimit != 0) {
            long confirmedRequests = requestRepository.countConfirmedRequestsByEventId(event.getId());
            //+ если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
            if (confirmedRequests == participantLimit) {
                throw new RequestLimitException("У события достигнут лимит подтвержденных заявок");
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getAll(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.debug("GET USER. Пользователь с айди {} не найден.", userId);
                    return new DataNotFoundException("Пользователь с id=" + userId + " не существует.");
                });
        return requestRepository.findRequestsByUser(userId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancel(long userId, long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.debug("GET REQUEST. Заявка с айди {} не найденf.", requestId);
                    return new DataNotFoundException("Заявка с id=" + requestId + " не существует.");
                });
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.debug("GET USER. Пользователь с айди {} не найден.", userId);
                    return new DataNotFoundException("Пользователь с id=" + userId + " не существует.");
                });
        if (request.getRequester().getId() != userId) {
            throw new IllegalStateException("Отмена заявки на событие невозможна. У вас нет активных заявок на это событие.");
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toDto(request);
    }
}
