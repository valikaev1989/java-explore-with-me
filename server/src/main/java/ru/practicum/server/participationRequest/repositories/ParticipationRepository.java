package ru.practicum.server.participationRequest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.participationRequest.models.ParticipationRequest;
import ru.practicum.server.utils.State;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> getAllByUser_UserId(Long userId);

    @Query("select p from ParticipationRequest p where p.event.initiator.userId = ?1 and p.event.eventId = ?2")
    List<ParticipationRequest> getOwnerEventRequests(Long userId, Long eventId);

    @Query("select p from ParticipationRequest p where p.event = ?1 and p.status <> ?2")
    List<ParticipationRequest> getAllByEventAndAndStatusNot(Event event, State state);

    @Query("select (count(p) > 0) from ParticipationRequest p " +
            "where p.event.eventId = ?1 and p.user.userId = ?2 and p.status = ?3 or p.status = ?4")
    Boolean checkDuplicateRequest(Long eventId, Long userId, State state1, State state2);
}