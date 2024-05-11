package ru.practicum.ewm.events.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    public Page<Event> findByInitiatorId(Long userId, PageRequest page);

    public Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    @Query(value =
            "SELECT e FROM Event AS e WHERE e.initiator.id IN (:users) AND " +
                    "e.state IN (:states) AND e.category.id IN (:categories) AND e.eventDate >= :rangeStart " +
                    "AND e.eventDate < :rangeEnd")
    public Page<Event> findByFilter(List<Long> users, List<State> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest page);

    @Query(value =
            "SELECT e FROM Event AS e WHERE " +
                    "e.state IN (:states) AND e.category.id IN (:categories) AND e.eventDate >= :rangeStart " +
                    "AND e.eventDate < :rangeEnd")
    public Page<Event> findByFilterWithoutUsers(List<State> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest page);

    @Query(value =
            "SELECT e FROM Event AS e WHERE e.initiator.id IN (:users) AND " +
                    "e.state IN (:states) AND e.eventDate >= :rangeStart " +
                    "AND e.eventDate < :rangeEnd")
    public Page<Event> findByFilterWithoutCategories(List<Long> users, List<State> states,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest page);

    @Query(value =
            "SELECT e FROM Event AS e WHERE " +
                    "e.state IN (:states) AND e.eventDate >= :rangeStart " +
                    "AND e.eventDate < :rangeEnd")
    public Page<Event> findByFilterWithoutAll(List<State> states,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest page);

    public Boolean existsByIdAndInitiatorId(Long eventId, Long userId);

    public Boolean existsByCategoryId(Long catId);

    public Optional<Event> findByIdAndState(Long eventId, State state);

    @Query(value =
            "SELECT e FROM Event AS e WHERE LOWER(e.annotation) LIKE CONCAT('%', :text, '%') " +
            "OR LOWER(e.description) LIKE CONCAT('%', :text, '%') AND e.category.id IN (:categories) " +
            "AND e.paid = :paid AND e.eventDate >= :rangeStart AND e.eventDate < :rangeEnd AND e.state = :state")
    public Page<Event> findByFilter(String text, List<Long> categories, Boolean paid,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    State state, PageRequest page);

    @Query(value =
            "SELECT e FROM Event AS e WHERE " +
            "e.category.id IN (:categories) " +
            "AND e.paid = :paid AND e.eventDate >= :rangeStart AND e.eventDate < :rangeEnd AND e.state = :state")
    public Page<Event> findByFilterWithoutText(List<Long> categories, Boolean paid,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    State state, PageRequest page);

    @Query(value =
            "SELECT e FROM Event AS e WHERE LOWER(e.annotation) LIKE CONCAT('%', :text, '%') " +
            "OR LOWER(e.description) LIKE CONCAT('%', :text, '%') AND e.category.id IN (:categories) " +
            "AND e.eventDate >= :rangeStart AND e.eventDate < :rangeEnd AND e.state = :state")
    public Page<Event> findByFilterWithoutPaid(String text, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    State state, PageRequest page);

    @Query(value =
            "SELECT e FROM Event AS e WHERE " +
                    "e.category.id IN (:categories) " +
                    "AND e.eventDate >= :rangeStart AND e.eventDate < :rangeEnd AND e.state = :state")
    public Page<Event> findByFilterWithoutAll(List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    State state, PageRequest page);
}
