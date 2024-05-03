package ru.practicum.ewm.events.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.events.model.Location;
import ru.practicum.ewm.events.model.State;
import ru.practicum.ewm.users.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.Configuration.DATE_TIME_FORMAT;

@Data
public class EventFullDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    private String description;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Long participantLimit = 0L;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;

    private Boolean requestModeration = true;

    private State state;

    private String title;

    private Location views;
}
