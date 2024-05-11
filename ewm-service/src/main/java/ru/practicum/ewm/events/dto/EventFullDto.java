package ru.practicum.ewm.events.dto;

import lombok.Data;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.events.model.Location;
import ru.practicum.ewm.events.model.State;
import ru.practicum.ewm.users.dto.UserShortDto;

@Data
public class EventFullDto {

    private Long id;

    private String title;

    private String annotation;

    private String description;

    private String eventDate;

    private Location location;

    private Integer participantLimit = 0;

    private Boolean paid;

    private Boolean requestModeration = true;

    private State state;

    private String createdOn;

    private String publishedOn;

    private Long views;

    private CategoryDto category;

    private UserShortDto initiator;

    private Long confirmedRequests = 0L;
}
