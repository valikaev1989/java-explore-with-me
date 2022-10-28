package ru.practicum.server.participationRequest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.participationRequest.models.ParticipationRequest;
import ru.practicum.server.utils.State;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> getAllByUser_UserId(Long userId);

    /**
     *
     * getAllByEvent_Initiator_UserIdAndAndEvent_EventId
     */
    @Query("select p from ParticipationRequest p where p.event.initiator.userId = ?1 and p.event.eventId = ?2")
    List<ParticipationRequest> getOwnerEventRequests(Long userId, Long eventId);

    /**
     * @param event
     * @param state
     * @return
     */
    @Query("select p from ParticipationRequest p where p.event = ?1 and p.status <> ?2")
    List<ParticipationRequest> getAllByEventAndAndStatusNot(Event event, State state);

//    /**
//     * findAllByUser_UserIdAndEvent_EventIdAndEvent_Initiator_UserIdAndStateNot
//     */
//    @Query("select p from ParticipationRequest p " +
//            "where p.user.userId = ?1 and p.event.eventId = ?2 and p.event.initiator.userId = ?1 and p.state <> ?3")
//    List<ParticipationRequest> checkDuplicateRequest(Long userID, Long eventId, State state);

    /**
     * existsByEvent_EventIdAndAndUser_UserIdAndStateOrState
     */
    @Query("select (count(p) > 0) from ParticipationRequest p " +
            "where p.event.eventId = ?1 and p.user.userId = ?2 and p.status = ?3 or p.status = ?4")
    Boolean checkDuplicateRequest(Long eventId, Long userId, State state1, State state2);

//    /**
//     * countByEvent_EventIdAndAndState
//     */
//    @Query("select count(p) from ParticipationRequest p where p.event.eventId = ?1 and p.state = ?2")
//    Integer countConfirmedRequest(Long eventId, State state);
}