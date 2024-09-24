package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.DataNotFoundException;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiatorId(long userId, Pageable page);

    default Event checkAndGetEvent(long eventId) {
        return this.findById(eventId)
                .orElseThrow(() -> {
//                    log.debug("GET USER. Событие с айди {} не найден.", eventId);
                    return new DataNotFoundException("Событие с id=" + eventId + " не существует.");
                });
    }
}
