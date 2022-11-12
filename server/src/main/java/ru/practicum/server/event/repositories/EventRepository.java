package ru.practicum.server.event.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventDtos.AdminParamsDto;
import ru.practicum.server.utils.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.initiator.userId = ?1")
    List<Event> findAllByInitiator_UserId(Long userId, Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate between :rangeStart and :rangeEnd " +
            "and ((:categoriesIds) is null OR e.category.categoryId in :categoriesIds) " +
            "and ((:stateList) is null OR e.state in :stateList) " +
            "and ((:userIds) is null OR e.initiator.userId in :userIds) " +
            "order by e.eventDate DESC")
    List<Event> getAllEventsByParametersForAdmin(List<Long> userIds, List<Long> categoriesIds,
                                                 List<State> stateList, LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where e.category.categoryId = :catId")
    List<Event> getAllByCategory_CategoryId(@Param("catId") Long catId);

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

//    @Query(value = "SELECT e.* FROM events AS e " +
//            "LEFT JOIN (" +
//            "   SELECT COUNT(r.id) AS COUNT, event_id FROM requests AS r " +
//            "   WHERE r.status = 3 GROUP BY event_id" +
//            ") AS rcount ON rcount.event_id = e.id " +
//            "WHERE (false = :searchByText OR UPPER(e.annotation) like :text OR UPPER(e.description) like :text) " +
//            "AND (false = :searchByCategory OR e.category_id IN :categoryIds) " +
//            "AND (false = :searchByIsPaid OR e.is_paid = :isPaid) " +
//            "AND (false = :searchByOneDate OR e.event_date > :rangeStart) " +
//            "AND (true = :searchByOneDate OR e.event_date between :rangeStart and :rangeEnd) " +
//            "AND (false = :searchByOnlyAvailable OR e.participant_limit = 0 OR e.participant_limit > rcount.COUNT)" +
//            "ORDER BY event_date DESC",
//            nativeQuery = true)
//    List<Event> findEventsByParams(
//            @Param("searchByText")boolean searchByText,
//            @Param("text")String text,
//            @Param("searchByCategory")boolean searchByCategory,
//            @Param("categoryIds") Set<Integer> categoryIds,
//            @Param("searchByIsPaid")boolean searchByIsPaid,
//            @Param("isPaid")Boolean isPaid,
//            @Param("searchByOneDate")boolean searchByOneDate,
//            @Param("rangeStart")LocalDateTime rangeStart,
//            @Param("rangeEnd")LocalDateTime rangeEnd,
//            @Param("searchByOnlyAvailable")boolean searchByOnlyAvailable,
//            Pageable pageable
//    );
//
//    @Query(value = "SELECT e.* FROM events AS e " +
//            "WHERE (false = :searchByUsers OR e.initiator_id IN :userIds) " +
//            "AND (false = :searchByStates OR e.status IN :stateIds) " +
//            "AND (false = :searchByCategory OR e.category_id IN :categoryIds) " +
//            "AND (true = :searchByOneDate OR e.event_date > :rangeStart) " +
//            "AND (false = :searchByOneDate OR e.event_date between :rangeStart and :rangeEnd)",
//            nativeQuery = true)
//    List<Event> getAllEventsByParametersForAdmin(
//            @Param("searchByUsers")boolean searchByUsers,
//            @Param("userIds") Set<Integer> userIds,
//            @Param("searchByStates")boolean searchByStates,
//            @Param("stateIds") Set<Integer> stateIds,
//            @Param("searchByCategory")boolean searchByCategory,
//            @Param("categoryIds") Set<Integer> categoryIds,
//            @Param("searchByOneDate")boolean searchByOneDate,
//            @Param("rangeStart")LocalDateTime rangeStart,
//            @Param("rangeEnd")LocalDateTime rangeEnd,
//            Pageable pageable
//    );


//    @Query(value = "SELECT e FROM Event e " +
//            "WHERE ((:text IS NULL OR lower(e.annotation) LIKE concat('%', :text, '%')) " +
//            "OR (:text IS NULL OR lower(e.description) LIKE concat('%', :text, '%'))) " +
//            "AND (coalesce(:categories, null) IS NULL OR e.category.id IN :categories) " +
//            "AND (coalesce(:paid, null) IS NULL OR e.paid = :paid) " +
//            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
//            "AND e.state = 'PUBLISHED'")
//    List<Event> getPublishedEvents(@Param("text") String text, @Param("categories") List<Integer> categories,
//                                   @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
//                                   @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

//    @Query(value = "SELECT e FROM Event e " +
//            "WHERE (coalesce(:userIds, null) IS NULL OR e.initiator.id IN :userIds) " +
//            "AND (coalesce(:states, null) IS NULL OR e.state IN :states) " +
//            "AND (coalesce(:categoryIds, null) IS NULL OR e.category.id IN :categoryIds) " +
//            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
//    List<Event> getAllEventsByParametersForAdmin(@Param("userIds") List<Integer> userIds, @Param("states") List<State> states,
//                               @Param("categoryIds") List<Integer> categoryIds,
//                               @Param("rangeStart") LocalDateTime rangeStart,
//                               @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

//    @Query("SELECT e FROM Event AS e " +
//            "WHERE ((:text) IS NULL " +
//            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')) " +
//            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%'))) " +
//            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
//            "AND ((:paid) IS NULL OR e.paid = :paid) " +
//            "AND (e.eventDate >= :start) " +
//            "AND ( e.eventDate <= :end)")
//    Page<Event> searchEvents(String text, List<Long> categories, Boolean paid, LocalDateTime start,
//                             LocalDateTime end, Pageable pageable);
//
//    @Query("SELECT e FROM Event AS e " +
//            "WHERE ((:users) IS NULL OR e.initiator.id IN :users) " +
//            "AND ((:states) IS NULL OR e.state IN :states) " +
//            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
//            "AND (e.eventDate >= :start) " +
//            "AND ( e.eventDate <= :end)")
//    Page<Event> getAllEventsByParametersForAdmin(List<Long> users, List<State> states, List<Long> categories,
//                                    LocalDateTime start, LocalDateTime end, Pageable pageable);
//}