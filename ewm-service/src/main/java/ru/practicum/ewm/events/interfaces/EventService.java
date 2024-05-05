package ru.practicum.ewm.events.interfaces;

import ru.practicum.ewm.events.dto.*;
import ru.practicum.ewm.events.model.Sort;
import ru.practicum.ewm.events.model.State;

import java.time.LocalDateTime;
import java.util.List;


public interface EventService {

    public EventFullDto addEvent(NewEventDto newEventDto, Long userId);

    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    public EventFullDto getEvent(Long eventId, Long userId);

    public EventFullDto updateEventByUser(UpdateEventUserRequest updateEventUserRequest, Long eventId, Long userId);

    public List<EventFullDto> getEventsAdminByFilter(List<Long> users, List<State> states, List<Long> categories,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                                     Integer size);

    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEventUserRequest, Long eventId);

    public List<EventShortDto> getEventsByFilter(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Boolean onlyAvailable, Sort sort, Integer from, Integer size);
}
