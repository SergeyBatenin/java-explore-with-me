package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.AdminParamSearchEvent;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventWithCountConfirmedRequests;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.PublicParamSearchEvent;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.dto.mapper.EventMapper;
import ru.practicum.event.model.AdminStateAction;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.model.State;
import ru.practicum.event.model.UserStateAction;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.ewm.StatClient;
import ru.practicum.ewm.dto.ParamHitDto;
import ru.practicum.ewm.dto.ParamStatDto;
import ru.practicum.ewm.dto.StatInfoDto;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.NotAvailableException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.mapper.RequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final StatClient statClient;

    //region PrivateApi
    @Transactional
    @Override
    public EventFullDto create(long userId, NewEventDto eventDto) {
        User user = checkAndGetUser(userId);
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> {
                    log.debug("GET CATEGORY. Категории с айди {} не найден.", eventDto.getCategory());
                    return new DataNotFoundException("Категории с id=" + eventDto.getCategory() + " не существует.");
                });
        Event event = eventMapper.toEvent(eventDto, user, category);
        eventRepository.save(event);
        return eventMapper.toFullDto(event, 0L, 0L);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getAll(long userId, int from, int size) {
        checkAndGetUser(userId);

        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);

        Map<Long, Event> events = eventRepository.findAllByInitiatorId(userId, page).stream()
                .collect(Collectors.toMap(Event::getId, Function.identity()));

        return events.values().stream()
                .map(event -> eventMapper.toShortDto(event, 0, 0))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getById(long userId, long eventId) {
        checkAndGetUser(userId);
        Event event = checkAndGetEvent(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new NotAvailableException("Нет доступа к запрашиваемому событию.");
        }

        return eventMapper.toFullDto(event, 0L, 0L);
    }

    @Transactional
    @Override
    public EventFullDto updateByCreator(long userId, long eventId, UpdateEventUserRequest updateRequest) {
        Event event = checkAndGetEvent(eventId);
        if (event.getState() == State.PUBLISHED) {
            throw new IllegalStateException("Изменение опубликованных событий недопустимо");
        }

        checkAndGetUser(userId);

        if (event.getInitiator().getId() != userId) {
            throw new NotAvailableException("Нет доступа к запрашиваемому событию.");
        }
        userUpdateEvent(event, updateRequest);

        return eventMapper.toFullDto(event, 0L, 0L);
    }

    private void userUpdateEvent(Event event, UpdateEventUserRequest updateRequest) {
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getCategory() != null) {
            event.getCategory().setId(updateRequest.getCategory());
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(updateRequest.getLocation());
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        UserStateAction userStateAction = updateRequest.getStateAction();
        if (userStateAction != null) {
            if (userStateAction == UserStateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PENDING);
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getRequests(long userId, long eventId) {
        checkAndGetUser(userId);
        Event event = checkAndGetEvent(eventId);
        if (event.getInitiator().getId() != userId) {
            return Collections.emptyList();
        }

        return requestRepository.findRequestsByEventId(eventId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult replyToRequests(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest) {
        checkAndGetUser(userId);
        Event event = checkAndGetEvent(eventId);

        if (event.getState() != State.PUBLISHED) {
            throw new IllegalStateException("Событие должно быть опубликовано!");
        }

        List<ParticipationRequest> pendingRequests =
                requestRepository.findPendingRequestsByEventIdAndIdIn(eventId, updateRequest.getRequestIds());

        // статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
        if (updateRequest.getRequestIds().size() != pendingRequests.size()) {
            throw new IllegalArgumentException("Подтверждать завки можно только со статусом в \"В ожидании\"");
        }
        if (updateRequest.getStatus() == RequestStatus.REJECTED) {
            return rejectRequests(pendingRequests);
        } else if (updateRequest.getStatus() == RequestStatus.CONFIRMED) {
            // если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
            int participantLimit = event.getParticipantLimit();
            if (participantLimit == 0 || !event.isRequestModeration()) {
                return confirmAllRequests(pendingRequests);
            } else {
                // нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
                long countConfirmedRequests = requestRepository.countConfirmedRequestsByEventId(eventId);
                if (countConfirmedRequests == participantLimit) {
                    throw new IllegalArgumentException("На событие уже зарегистрировано максимальное количество человек");
                }
                return confirmRequestsWithLimit(pendingRequests, countConfirmedRequests, participantLimit);
            }
        }
        return null;
    }

    private EventRequestStatusUpdateResult confirmRequestsWithLimit(List<ParticipationRequest> pendingRequests,
                                                                    long countConfirmedRequests,
                                                                    int participantLimit) {
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();

        for (ParticipationRequest request : pendingRequests) {
            if (countConfirmedRequests < participantLimit) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmed.add(requestMapper.toDto(request));
                countConfirmedRequests++;
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejected.add(requestMapper.toDto(request));
            }
        }
        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }

    private EventRequestStatusUpdateResult confirmAllRequests(List<ParticipationRequest> pendingRequests) {
        List<ParticipationRequestDto> confirmedRequests = pendingRequests.stream()
                .peek(request -> request.setStatus(RequestStatus.CONFIRMED))
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(confirmedRequests, new ArrayList<>());
    }

    private EventRequestStatusUpdateResult rejectRequests(List<ParticipationRequest> pendingRequests) {
        List<ParticipationRequestDto> rejectedRequests = pendingRequests.stream()
                .peek(request -> request.setStatus(RequestStatus.REJECTED))
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(new ArrayList<>(), rejectedRequests);
    }

    private User checkAndGetUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.debug("GET USER. Пользователь с айди {} не найден.", userId);
                    return new DataNotFoundException("Пользователь с id=" + userId + " не существует.");
                });
    }

    private Event checkAndGetEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.debug("GET EVENT. Событие с айди {} не найден.", eventId);
                    return new DataNotFoundException("Событие с id=" + eventId + " не существует.");
                });
    }
    //endregion PrivateApi

    //region AdminApi
    @Transactional
    @Override
    public EventFullDto updateByAdmin(long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = checkAndGetEvent(eventId);
        if (updateRequest.getStateAction() == AdminStateAction.PUBLISH_EVENT
                && event.getState() != State.PENDING) {
            throw new IllegalStateException("Опубликовать события можно только в состоянии \"В ожидании\"");
        }
        if (updateRequest.getStateAction() == AdminStateAction.REJECT_EVENT
                && event.getState() == State.PUBLISHED) {
            throw new IllegalStateException("Опубликованные события нельзя отменить");
        }
        adminUpdateEvent(event, updateRequest);
        return eventMapper.toFullDto(event, 0L, 0L);
    }

    private void adminUpdateEvent(Event event, UpdateEventAdminRequest updateRequest) {
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getCategory() != null) {
            event.getCategory().setId(updateRequest.getCategory());
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(updateRequest.getLocation());
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        AdminStateAction adminStateAction = updateRequest.getStateAction();
        if (adminStateAction != null) {
            if (adminStateAction == AdminStateAction.REJECT_EVENT) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> search(AdminParamSearchEvent paramSearchEvent) {
        Pageable page = PageRequest.of(paramSearchEvent.getFrom() > 0
                        ? paramSearchEvent.getFrom() / paramSearchEvent.getSize()
                        : 0,
                paramSearchEvent.getSize());

        Optional<BooleanExpression> queryParams = makeAdminParamsSearch(paramSearchEvent);
        Map<Long, Event> events = queryParams
                .map(booleanExpression -> eventRepository.findAll(booleanExpression, page).getContent())
                .orElseGet(() -> eventRepository.findAll(page).getContent()).stream()
                .collect(Collectors.toMap(Event::getId, Function.identity()));

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Long> eventWithConfirmedRequests = requestRepository.countConfirmedRequestsByEventIdIn(events.keySet()).stream()
                .collect(Collectors.toMap(
                        EventWithCountConfirmedRequests::getEventId,
                        EventWithCountConfirmedRequests::getConfirmedRequests));

        return events.values().stream()
                .map(event -> eventMapper.toFullDto(
                        event,
                        eventWithConfirmedRequests.getOrDefault(event.getId(), 0L),
                        0L))
                .collect(Collectors.toList());
    }

    private Optional<BooleanExpression> makeAdminParamsSearch(AdminParamSearchEvent paramSearchEvent) {
        List<BooleanExpression> queryParams = new ArrayList<>();
        if (paramSearchEvent.getUsers() != null && !paramSearchEvent.getUsers().isEmpty()) {
            queryParams.add(QEvent.event.initiator.id.in(paramSearchEvent.getUsers()));
        }
        if (paramSearchEvent.getCategories() != null && !paramSearchEvent.getCategories().isEmpty()) {
            queryParams.add(QEvent.event.category.id.in(paramSearchEvent.getCategories()));
        }
        if (paramSearchEvent.getStates() != null && !paramSearchEvent.getStates().isEmpty()) {
            queryParams.add(QEvent.event.state.in(paramSearchEvent.getStates()));
        }
        if (paramSearchEvent.getRangeEnd() != null) {
            queryParams.add(QEvent.event.eventDate.before(paramSearchEvent.getRangeEnd()));
        }
        if (paramSearchEvent.getRangeStart() != null) {
            queryParams.add(QEvent.event.eventDate.after(paramSearchEvent.getRangeStart()));
        }
        return queryParams.stream().reduce(BooleanExpression::and);
    }
    //endregion AdminApi

    //region PublicApi
    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> showEvents(PublicParamSearchEvent paramSearchEvent, HttpServletRequest request) {

        Pageable page = PageRequest.of(paramSearchEvent.getFrom() > 0
                        ? paramSearchEvent.getFrom() / paramSearchEvent.getSize()
                        : 0,
                paramSearchEvent.getSize());

        BooleanExpression booleanExpression = makePublicParamsSearch(paramSearchEvent);

        Map<Long, Event> events = eventRepository.findAll(booleanExpression, page).getContent().stream()
                .collect(Collectors.toMap(Event::getId, Function.identity()));
        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Long> eventWithConfirmedRequests = requestRepository.countConfirmedRequestsByEventIdIn(events.keySet()).stream()
                .collect(Collectors.toMap(
                        EventWithCountConfirmedRequests::getEventId,
                        EventWithCountConfirmedRequests::getConfirmedRequests));

        Map<String, Long> uris = new HashMap<>();
        LocalDateTime earliestDate = LocalDateTime.now();
        for (Event event : events.values()) {
            uris.put("/events/" + event.getId(), event.getId());
            if (event.getPublishedOn().isBefore(earliestDate)) {
                earliestDate = event.getPublishedOn();
            }
        }

        ParamStatDto paramStatDto = new ParamStatDto(
                earliestDate,
                LocalDateTime.now(),
                new ArrayList<>(uris.keySet()),
                true);
        Map<Long, Long> eventsWithViews = statClient.getStats(paramStatDto).stream()
                .collect(Collectors.toMap(
                        statInfoDto -> uris.get(statInfoDto.getUri()),
                        StatInfoDto::getHits));

        sendStats(request);

        Stream<Event> eventStream;
        if (paramSearchEvent.getOnlyAvailable()) {
            eventStream = events.values().stream()
                    .filter(event -> event.getParticipantLimit() == 0 || event.getParticipantLimit() > eventWithConfirmedRequests.getOrDefault(event.getId(), 0L));
        } else {
            eventStream = events.values().stream();
        }

        return eventStream
                .map(event -> eventMapper.toShortDto(
                        event,
                        eventWithConfirmedRequests.getOrDefault(event.getId(), 0L),
                        eventsWithViews.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    private BooleanExpression makePublicParamsSearch(PublicParamSearchEvent paramSearchEvent) {
        List<BooleanExpression> queryParams = new ArrayList<>();
        queryParams.add(QEvent.event.state.eq(State.PUBLISHED));
        if (paramSearchEvent.getText() != null && !paramSearchEvent.getText().isEmpty()) {
            queryParams.add(
                    QEvent.event.annotation.containsIgnoreCase(paramSearchEvent.getText())
                            .or(QEvent.event.description.containsIgnoreCase(paramSearchEvent.getText()))
            );
        }
        if (paramSearchEvent.getCategories() != null && !paramSearchEvent.getCategories().isEmpty()) {
            queryParams.add(QEvent.event.category.id.in(paramSearchEvent.getCategories()));
        }
        if (paramSearchEvent.getPaid() != null) {
            queryParams.add(QEvent.event.paid.eq(paramSearchEvent.getPaid()));
        }
        if (paramSearchEvent.getRangeStart() != null && paramSearchEvent.getRangeEnd() != null) {
            queryParams.add(
                    QEvent.event.eventDate.between(paramSearchEvent.getRangeStart(), paramSearchEvent.getRangeEnd()));
        } else if (paramSearchEvent.getRangeStart() != null) {
            queryParams.add(QEvent.event.eventDate.after(paramSearchEvent.getRangeStart()));
        } else if (paramSearchEvent.getRangeEnd() != null) {
            queryParams.add(QEvent.event.eventDate.between(LocalDateTime.now(), paramSearchEvent.getRangeEnd()));
        } else {
            queryParams.add(QEvent.event.eventDate.after(LocalDateTime.now()));
        }

        return queryParams.stream().reduce(BooleanExpression::and).get();
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto showById(long eventId, HttpServletRequest request) {
        Event event = checkAndGetEvent(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new DataNotFoundException("Событие не опубликовано");
        }

        long countConfirmedRequests = requestRepository.countConfirmedRequestsByEventId(eventId);
        List<String> uris = new ArrayList<>();
        uris.add(request.getRequestURI());
        ParamStatDto paramStatDto = new ParamStatDto(event.getCreatedOn(), LocalDateTime.now(), uris, true);
        List<StatInfoDto> stats = statClient.getStats(paramStatDto);

        EventFullDto fullDto = eventMapper.toFullDto(
                event,
                countConfirmedRequests,
                stats.isEmpty() ? 0 : stats.getFirst().getHits());

        sendStats(request);
        return fullDto;
    }

    private void sendStats(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        String ip = xForwardedForHeader == null ? request.getRemoteAddr() : xForwardedForHeader.split(",")[0];
        ParamHitDto paramHitDto = new ParamHitDto("ewm-main-service", request.getRequestURI(), ip, LocalDateTime.now());

        statClient.save(paramHitDto);
    }
    //endregion PublicApi
}
