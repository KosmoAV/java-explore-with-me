package ru.practicum.ewm.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.Configuration;
import ru.practicum.ewm.categories.interfaces.CategoryRepository;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.events.dto.*;
import ru.practicum.ewm.events.interfaces.EventRepository;
import ru.practicum.ewm.events.interfaces.EventService;
import ru.practicum.ewm.events.model.Sort;
import ru.practicum.ewm.events.model.State;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictRequestException;
import ru.practicum.ewm.exception.DataNotFoundRequestException;
import ru.practicum.ewm.request.interfaces.RequestRepository;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.users.interfaces.UserRepository;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.events.model.Event;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public EventFullDto addEvent(NewEventDto newEventDto, Long userId) {

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Event date cannot be earlier than two hours later");
        }

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new DataNotFoundRequestException("Category with id = "
                        + newEventDto.getCategory() + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundRequestException("User with id = "
                        + userId + " not found"));

        Event event = eventRepository.save(EventMapper.toEvent(newEventDto, category, user));

        Long confirmedRequests = requestRepository.getCountRequest(event.getId(), Status.CONFIRMED).orElse(0L);

        return EventMapper.toEventFullDto(event, confirmedRequests);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<Event> events = eventRepository.findByInitiatorId(userId, page).getContent();

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);

        return events.stream()
                .map(event -> EventMapper.toEventShortDto(event, confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Long eventId, Long userId) {

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new DataNotFoundRequestException("Event with id = " + eventId +
                        " and initiatorId = " + userId + "not found"));

        Long confirmedRequests = requestRepository.getCountRequest(eventId, Status.CONFIRMED).orElse(0L);
        return EventMapper.toEventFullDto(event, confirmedRequests);
    }

    @Override
    @Transactional
    public EventFullDto getEvent(Long eventId, String remoteAddress) {

        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new DataNotFoundRequestException("Published event with id = " + eventId + " not found"));

        if (!event.getIps().contains(remoteAddress)) {
            event.getIps().add(remoteAddress);
            event.setViews(event.getViews() + 1);

            eventRepository.save(event);
        }

        Long confirmedRequests = requestRepository.getCountRequest(eventId, Status.CONFIRMED).orElse(0L);
        return EventMapper.toEventFullDto(event, confirmedRequests);
    }

    @Override
    public EventFullDto updateEventByUser(UpdateEventUserRequest updateEventUserRequest, Long eventId, Long userId) {

        if (updateEventUserRequest.getEventDate() != null &&
                updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Event date cannot be earlier than two hours later");
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new DataNotFoundRequestException("Event with id = " + eventId +
                        " and initiatorId = " + userId + "not found"));

        if (event.getState() == State.PUBLISHED) {
            throw new ConflictRequestException("The event has already been published");
        }

        event = eventRepository.save(changeEventByUser(event, updateEventUserRequest));

        Long confirmedRequests = requestRepository.getCountRequest(eventId, Status.CONFIRMED).orElse(0L);

        return EventMapper.toEventFullDto(event, confirmedRequests);
    }

    @Override
    public List<EventFullDto> getEventsAdminByFilter(List<Long> users, List<State> states, List<Long> categories,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<Event> events = eventRepository.findByFilter(users, states, categories, rangeStart, rangeEnd, page).getContent();

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);

        System.out.println(confirmedRequests);

        return events.stream()
                .map(event -> EventMapper.toEventFullDto(event, confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundRequestException("Event with id = " + eventId
                        + "not found"));

        event = eventRepository.save(changeEventByAdmin(event, updateEventAdminRequest));

        Long confirmedRequests = requestRepository.getCountRequest(eventId, Status.CONFIRMED).orElse(0L);

        return EventMapper.toEventFullDto(event, confirmedRequests);
    }

    @Override
    public List<EventShortDto> getEventsByFilter(String text, List<Long> categories, Boolean paid,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable, Sort sort, Integer from, Integer size) {

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<Event> events;

        if (rangeStart == null || rangeEnd == null) {
            events = eventRepository.findByFilter(text.toLowerCase(), categories, paid,  State.PUBLISHED,
                                                  page).getContent();
        } else {

            if (!rangeEnd.isAfter(rangeStart)) {
                throw new BadRequestException("Range end must be after range start");
            }

            events = eventRepository.findByFilter(text.toLowerCase(), categories, paid,
                                                  rangeStart, rangeEnd, State.PUBLISHED, page).getContent();
        }

        System.out.println("events = " + events);

        if (events.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);

        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getParticipantLimit() > confirmedRequests.get(event.getId()))
                    .collect(Collectors.toList());

            System.out.println("filter events = " + events);
        }

        if (sort != null) {
            switch (sort) {
                case VIEWS:
                    events = events.stream()
                            .sorted(Comparator.comparingLong(Event::getViews))
                            .collect(Collectors.toList());
                    break;
                case EVENT_DATE:
                    events = events.stream()
                            .sorted(this::compareEventForDate)
                            .collect(Collectors.toList());
                    break;
            }
        }

        System.out.println("Sort events = " + events);

        return events.stream()
                .map(event -> EventMapper.toEventShortDto(event, confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());
    }

    private int compareEventForDate(Event a, Event b) {
        return a.getEventDate().compareTo(b.getEventDate());
    }

    private Event changeEventByUser(Event event, UpdateEventUserRequest newEvent) {

        changeEvent(event, newEvent);

        if (newEvent.getStateAction() != null) {

            switch (newEvent.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                break;
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                break;
            }
        }

        return event;
    }

    private Event changeEventByAdmin(Event event, UpdateEventAdminRequest newEvent) {

        changeEvent(event, newEvent);

        if (event.getState() == State.PUBLISHED) {
            if (event.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
                throw new ConflictRequestException("Event date should be no earlier than one hour after publication");
            }
        }

        if (newEvent.getStateAction() != null) {

            switch (newEvent.getStateAction()) {
                case PUBLISH_EVENT:

                    if (event.getState() != State.PENDING) {
                        throw new ConflictRequestException("Event should be in pending state");
                    }

                    LocalDateTime now = LocalDateTime.now();
                    if (event.getEventDate().isBefore(now.plusHours(1))) {
                        throw new ConflictRequestException("Event date should be no earlier than one hour after publication");
                    }

                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(now);
                break;
                case REJECT_EVENT:

                    if (event.getState() == State.PUBLISHED) {
                        throw new ConflictRequestException("Event should be in not published state");
                    }

                    event.setState(State.CANCELED);
                break;
            }
        }

        return event;
    }

    private <T extends BaseUpdateEventRequest> void changeEvent(Event event, T newEvent) {

        if (newEvent.getTitle() != null) {
            event.setTitle(newEvent.getTitle());
        }

        if (newEvent.getAnnotation() != null) {
            event.setAnnotation(newEvent.getAnnotation());
        }

        if (newEvent.getDescription() != null) {
            event.setDescription(newEvent.getDescription());
        }

        if (newEvent.getEventDate() != null) {

            if (!newEvent.getEventDate().isAfter(LocalDateTime.now())) {
                throw new BadRequestException("Event date must be in future");
            }

            event.setEventDate(newEvent.getEventDate());
        }

        if (newEvent.getLocation() != null) {
            event.setLat(newEvent.getLocation().getLat());
            event.setLon(newEvent.getLocation().getLon());
        }

        if (newEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(newEvent.getParticipantLimit());
        }

        if (newEvent.getPaid() != null) {
            event.setPaid(newEvent.getPaid());
        }

        if (newEvent.getRequestModeration() != null) {
            event.setRequestModeration(newEvent.getRequestModeration());
        }

        if (newEvent.getCategory() != null) {

            Category category = categoryRepository.findById(newEvent.getCategory())
                    .orElseThrow(() -> new DataNotFoundRequestException("Category with id = " +
                            newEvent.getCategory() + "not found"));

            event.setCategory(category);
        }
    }

    private Map<Long, Long> getConfirmedRequests(List<Event> events) {

        List<Long> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Map<Long, Long> map = requestRepository.getCountRequest(ids, Status.CONFIRMED).stream()
                .collect(Collectors.toMap(list -> list.get(0), list -> list.get(1)));

        for (Long id : ids) {
            if (!map.containsKey(id)) {
                map.put(id, 0L);
            }
        }

        return map;
    }
}
