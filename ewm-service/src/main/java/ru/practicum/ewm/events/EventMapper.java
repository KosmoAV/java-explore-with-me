package ru.practicum.ewm.events;

import ru.practicum.ewm.categories.CategoryMapper;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.Location;
import ru.practicum.ewm.events.model.State;
import ru.practicum.ewm.users.UserMapper;
import ru.practicum.ewm.events.dto.NewEventDto;
import ru.practicum.ewm.users.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewm.Configuration.DATE_TIME_FORMAT;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, Category category, User user) {

        Event event = new Event();

        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLat(newEventDto.getLocation().getLat());
        event.setLon(newEventDto.getLocation().getLon());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setPaid(newEventDto.getPaid());
        event.setRequestModeration(newEventDto.getRequestModeration());

        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setPublishedOn(null);
        event.setViews(0L);

        event.setCategory(category);
        event.setInitiator(user);

        return event;
    }

    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        EventFullDto eventFullDto = new EventFullDto();

        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate().format(formatter));
        eventFullDto.setLocation(new Location(event.getLat(), event.getLon()));
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setCreatedOn(event.getCreatedOn().format(formatter));

        if (event.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(event.getPublishedOn().format(formatter));
        } else {
            eventFullDto.setPublishedOn(null);
        }

        eventFullDto.setViews(event.getViews());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setInitiator(UserMapper.toShortUserDto(event.getInitiator()));
        eventFullDto.setConfirmedRequests(confirmedRequests);

        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event, Long confirmedRequests) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        EventShortDto eventShortDto = new EventShortDto();

        eventShortDto.setId(event.getId());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setEventDate(event.getEventDate().format(formatter));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setViews(event.getViews());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setInitiator(UserMapper.toShortUserDto(event.getInitiator()));
        eventShortDto.setConfirmedRequests(confirmedRequests);

        return eventShortDto;
    }
}
