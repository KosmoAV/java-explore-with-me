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

    public Boolean existsByIdAndInitiatorId(Long eventId, Long userId);
}
