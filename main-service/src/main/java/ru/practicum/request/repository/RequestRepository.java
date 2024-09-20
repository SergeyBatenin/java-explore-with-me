package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.dto.EventWithCountConfirmedRequests;
import ru.practicum.request.model.ParticipationRequest;

import java.util.List;
import java.util.Set;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    @Query("SELECT r " +
            "FROM ParticipationRequest r " +
            "WHERE r.requester.id = :requesterId " +
            "AND r.event.id = :eventId AND (r.status = 'PENDING' OR r.status = 'CONFIRMED')")
    List<ParticipationRequest> findByRequesterIdAndEventId(long requesterId, long eventId);

    @Query("SELECT COUNT(r) " +
            "FROM ParticipationRequest r " +
            "WHERE r.event.id = :eventId AND r.status = 'CONFIRMED'")
    long countConfirmedRequestsByEventId(Long eventId);

    @Query("SELECT new ru.practicum.event.dto.EventWithCountConfirmedRequests(r.event.id, COUNT(*)) " +
            "FROM ParticipationRequest r " +
            "WHERE r.event.id IN :eventIds AND r.status = 'CONFIRMED'" +
            "GROUP BY r.event.id")
    List<EventWithCountConfirmedRequests> countConfirmedRequestsByEventIdIn(Set<Long> eventIds);

    @Query("SELECT r " +
            "FROM ParticipationRequest r " +
            "JOIN Event e ON r.event.id = e.id " +
            "WHERE r.requester.id = :userId AND e.initiator.id <> :userId")
    List<ParticipationRequest> findRequestsByUser(Long userId);

    List<ParticipationRequest> findRequestsByEventId(long eventId);

    @Query("SELECT r " +
            "FROM ParticipationRequest r " +
            "WHERE r.event.id = :eventId AND r.id IN :ids AND r.status = 'PENDING'")
    List<ParticipationRequest> findPendingRequestsByEventIdAndIdIn(long eventId, List<Long> ids);
}
