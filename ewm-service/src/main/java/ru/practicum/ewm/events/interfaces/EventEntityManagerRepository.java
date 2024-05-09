package ru.practicum.ewm.events.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;


public interface EventEntityManagerRepository {

    public Page<Event> findByFilterAndPublished(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                PageRequest page);
}
