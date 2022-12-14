package ru.practicum.server.event.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.utils.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.initiator.userId = ?1")
    List<Event> findAllByInitiator_UserId(Long userId, Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.userId in ?1 " +
            "and e.category.categoryId in ?2 " +
            "and e.state in ?3 " +
            "and e.eventDate between ?4 and ?5 " +
            "order by e.eventDate DESC")
    List<Event> getAllEventsByParametersForAdmin(List<Long> userIds, List<Long> categoriesIds,
                                                 List<State> stateList, LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e " +
            "where e.state = ?1 " +
            "and e.category.categoryId in ?2 " +
            "and e.annotation like concat('%', ?3, '%') " +
            "and e.paid = ?4 " +
            "and e.eventDate between ?5 and ?6")
    List<Event> getAllEventsByParametersForPublic(
            State state, List<Long> categoriesIds, String text, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where e.initiator.userId = ?1")
    Event getByAndInitiator_UserId(Long userId);
}